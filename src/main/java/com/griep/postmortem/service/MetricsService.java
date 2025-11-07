package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.response.MetricsSeriesDTO;
import com.griep.postmortem.domain.dto.response.MetricsSeriesDTO.Point;
import com.griep.postmortem.domain.dto.response.MetricsSummaryDTO;
import com.griep.postmortem.domain.dto.response.MetricsSummaryDTO.*;
import com.griep.postmortem.domain.dto.response.MetricsSummaryDTO.ScoreKpi.ChecksCoverage;
import com.griep.postmortem.domain.dto.response.ScoreDTO;
import com.griep.postmortem.domain.enums.BucketEnum;
import com.griep.postmortem.domain.enums.DataFieldEnum;
import com.griep.postmortem.domain.enums.SeverityEnum;
import com.griep.postmortem.domain.enums.StatusEnum;
import com.griep.postmortem.domain.model.ActionItem;
import com.griep.postmortem.domain.model.Incident;
import com.griep.postmortem.domain.model.IncidentEvent;
import com.griep.postmortem.repository.ActionItemRepository;
import com.griep.postmortem.repository.IncidentEventRepository;
import com.griep.postmortem.repository.IncidentRepository;
import com.griep.postmortem.service.util.Accumulator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.griep.postmortem.domain.enums.ActionStatusEnum.DONE;
import static com.griep.postmortem.domain.enums.DataFieldEnum.ENDED;
import static com.griep.postmortem.service.util.MetricsUtils.*;
import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetricsService {

    private final IncidentRepository incidentRepository;
    private final IncidentEventRepository incidentEventRepository;
    private final ActionItemRepository actionItemRepository;
    private final ScoreService scoreService;


    // ===== SUMMARY =====
    public MetricsSummaryDTO summary(
            final LocalDate from,
            final LocalDate to,
            final DataFieldEnum dateField,             // STARTED || ENDED
            final List<SeverityEnum> severities,       // opcional
            final List<StatusEnum> statuses,           // opcional
            final boolean includeOpenInMttr            // default: false
    ) {
        var incidents = findIncidents(from, to, dateField);

        if (severities != null && !severities.isEmpty()) {
            incidents = incidents.stream()
                    .filter(incident ->
                            incident.getSeverity() != null &&
                                    severities.contains(incident.getSeverity()))
                    .toList();
        }
        if (statuses != null && !statuses.isEmpty()) {
            incidents = incidents.stream()
                    .filter(incident ->
                            incident.getStatus() != null &&
                                    statuses.contains(incident.getStatus()))
                    .toList();
        }

        // KPIs básicos (incidents)
        int total = incidents.size();
        int open = (int) incidents.stream()
                .filter(incident -> !"CLOSED".equalsIgnoreCase(incident.getStatus().name()))
                .count();
        int closed = total - open;
        Map<String, Integer> bySeverity =
                incidents.stream()
                        .collect(groupingBy(incident ->
                                        nullSafe(incident.getSeverity().name()),
                                collectingAndThen(counting(),
                                        Long::intValue)));

        // Pré-carregar dados auxiliares
        Map<Long, List<IncidentEvent>> eventsByIncident = loadEvents(incidents);
        Map<Long, List<ActionItem>> actionsByIncident = loadActions(incidents);

        // Métricas por incidente
        List<Integer> mttaMin = new ArrayList<>();
        List<Integer> mttrMin = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();

        int actionsTotal = 0;
        int actionsOpen = 0;
        int actionsDone = 0;
        int actionsOverdueOpen = 0;
        int doneOnTime = 0;
        int doneLate = 0;

        int checksCount = incidents.size();
        int checkMinEvents = 0;
        int checkImpact = 0;
        int checkWhys = 0;
        int checkRootCause = 0;
        int checkActionsBoth = 0;
        int checkCommunications = 0;

        for (Incident incident : incidents) {
            var events = eventsByIncident.getOrDefault(incident.getId(), List.of());
            var actionItems = actionsByIncident.getOrDefault(incident.getId(), List.of());

            // Score
            ScoreDTO score = scoreService.compute(incident);
            scores.add(score.score());
            if (score.checks().hasMinEvents()) {
                checkMinEvents++;
            }
            if (score.checks().hasImpact()) {
                checkImpact++;
            }
            if (score.checks().hasFiveWhys()) {
                checkWhys++;
            }
            if (score.checks().hasRootAndFactors()) {
                checkRootCause++;
            }
            if (score.checks().hasCorrectiveAndPreventiveWithOwnerAndDue()) {
                checkActionsBoth++;
            }
            if (score.checks().hasCommunication()) {
                checkCommunications++;
            }

            // MTTA/MTTR
            var metrics = computeTimes(incident, events, includeOpenInMttr);
            if (metrics.mttaMinutes() != null) {
                mttaMin.add(metrics.mttaMinutes());
            }
            if (metrics.mttrMinutes() != null) {
                mttrMin.add(metrics.mttrMinutes());
            }

            // Ações (SLA)
            actionsTotal += actionItems.size();
            for (ActionItem actionItem : actionItems) {
                boolean isDone = actionItem.getStatus() == DONE;
                if (isDone) {
                    actionsDone++;
                    if (actionItem.getDueDate() != null &&
                            actionItem.getCompletedAt() != null &&
                            !actionItem.getCompletedAt()
                                    .isAfter(actionItem.getDueDate())) {
                        doneOnTime++;
                    } else {
                        doneLate++;
                    }
                } else {
                    actionsOpen++;
                    if (actionItem.getDueDate() != null &&
                            now().isAfter(actionItem.getDueDate())) {
                        actionsOverdueOpen++;
                    }
                }
            }
        }

        // Distribuições
        var mttaDist = dist(mttaMin);
        var mttrDist = dist(mttrMin);
        var scoreDist = dist(scores);

        // Cobertura de checks (%)
        var checksPct = new ChecksCoverage(
                pct(checkMinEvents, checksCount),
                pct(checkImpact, checksCount),
                pct(checkWhys, checksCount),
                pct(checkRootCause, checksCount),
                pct(checkActionsBoth, checksCount),
                pct(checkCommunications, checksCount)
        );

        var onTimeRatePct = actionsDone == 0 ?
                null :
                roundPct((double) doneOnTime * 100.0 / actionsDone);

        return new MetricsSummaryDTO(
                new Window(
                        from.toString(),
                        to.toString(),
                        dateField),
                new Filters(
                        severities,
                        statuses),
                new IncidentsKpi(
                        total,
                        open,
                        closed,
                        bySeverity),
                new TimeKpi(
                        mttaDist,
                        mttrDist),
                new ScoreKpi(
                        scoreDist.avgMin(),
                        scoreDist.p50Min(),
                        scoreDist.p90Min(),
                        scoreDist.count(),
                        checksPct),
                new ActionsKpi(
                        actionsTotal,
                        actionsOpen,
                        actionsOverdueOpen,
                        actionsDone,
                        doneOnTime,
                        doneLate,
                        onTimeRatePct)
        );
    }

    // ===== SERIES =====
    public MetricsSeriesDTO series(
            final LocalDate from,
            final LocalDate to,
            final BucketEnum bucket,                    // DAY || WEEK || MONTH
            final DataFieldEnum dateField,              // STARTED || ENDED
            final Collection<SeverityEnum> severities   // opcional
    ) {
        List<Incident> incidents = findIncidents(from, to, dateField);
        if (severities != null && !severities.isEmpty()) {
            incidents = incidents.stream()
                    .filter(incident ->
                            incident.getSeverity() != null &&
                                    severities.contains(incident.getSeverity()))
                    .toList();
        }

        Map<Long, List<IncidentEvent>> eventsByIncident = loadEvents(incidents);
        Map<Long, List<ActionItem>> actionsByIncident = loadActions(incidents);

        // bucket key -> acumuladores
        Map<LocalDate, Accumulator> accumulators = new TreeMap<>();

        for (Incident incident : incidents) {
            LocalDate anchor = anchorDate(incident, dateField);
            if (anchor == null ||
                    anchor.isBefore(from) ||
                    anchor.isAfter(to)) {
                continue;
            }
            LocalDate key = bucketKey(anchor, bucket);

            var accumulator = accumulators.computeIfAbsent(key, k -> new Accumulator());

            // métricas p/ este incidente
            var score = scoreService.compute(incident);
            var times = computeTimes(incident, eventsByIncident.getOrDefault(incident.getId(), List.of()), false);

            accumulator.incrementIncidents();

            if (times.mttaMinutes() != null) {
                accumulator.getMtta().add(times.mttaMinutes());
            }
            if (times.mttrMinutes() != null) {
                accumulator.getMttr().add(times.mttrMinutes());
            }
            accumulator.getScores().add(score.score());

            // ações p/ taxa on-time
            for (ActionItem actionItem : actionsByIncident.getOrDefault(incident.getId(), List.of())) {
                if (actionItem.getStatus() == DONE) {
                    accumulator.incrementDone();
                    if (actionItem.getDueDate() != null &&
                            actionItem.getCompletedAt() != null &&
                            !actionItem.getCompletedAt().isAfter(actionItem.getDueDate())) {
                        accumulator.incrementDoneOnTime();
                    }
                }
            }
        }

        var points = accumulators.entrySet()
                .stream()
                .map(entry -> {
                    LocalDate k = entry.getKey();
                    Accumulator accumulator = entry.getValue();
                    return new Point(
                            k.toString(),
                            accumulator.getIncidents(),
                            calculateAverage(accumulator.getMtta()),
                            calculateAverage(accumulator.getMttr()),
                            calculateAverage(accumulator.getScores()),
                            accumulator.getDone() == 0 ?
                                    null :
                                    roundPct((double) accumulator.getDoneOnTime() * 100.0 / accumulator.getDone())
                    );
                })
                .toList();

        return new MetricsSeriesDTO(bucket, points);
    }

    private List<Incident> findIncidents(final LocalDate from,
                                         final LocalDate to,
                                         final DataFieldEnum dateField) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);
        if (ENDED.equals(dateField)) {
            return incidentRepository.findByEndedAtBetweenOrderByEndedAtAsc(start, end);
        }
        return incidentRepository.findByStartedAtBetweenOrderByStartedAtAsc(start, end);
    }

    private Map<Long, List<IncidentEvent>> loadEvents(final Collection<Incident> incidents) {
        if (incidents.isEmpty()) {
            return Map.of();
        }
        List<Long> ids = incidents.stream()
                .map(Incident::getId)
                .toList();
        List<IncidentEvent> all = incidentEventRepository.findByIncidentIdInOrderByEventAtAsc(ids);
        return all.stream()
                .collect(groupingBy(incidentEvent -> incidentEvent.getIncident().getId(), toList()));
    }

    private Map<Long, List<ActionItem>> loadActions(final Collection<Incident> incidents) {
        if (incidents.isEmpty()) {
            return Map.of();
        }
        List<Long> ids = incidents.stream()
                .map(Incident::getId)
                .toList();
        List<ActionItem> all = actionItemRepository.findByIncidentIdIn(ids);
        return all.stream()
                .collect(groupingBy(actionItem -> actionItem.getIncident().getId(), toList()));
    }
}