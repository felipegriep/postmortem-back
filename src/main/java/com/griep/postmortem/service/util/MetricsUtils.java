package com.griep.postmortem.service.util;

import com.griep.postmortem.domain.dto.response.MetricsSummaryDTO.TimeKpi.Dist;
import com.griep.postmortem.domain.enums.BucketEnum;
import com.griep.postmortem.domain.enums.DataFieldEnum;
import com.griep.postmortem.domain.enums.EventTypeEnum;
import com.griep.postmortem.domain.model.Incident;
import com.griep.postmortem.domain.model.IncidentEvent;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import static com.griep.postmortem.domain.enums.BucketEnum.MONTH;
import static com.griep.postmortem.domain.enums.BucketEnum.WEEK;
import static com.griep.postmortem.domain.enums.DataFieldEnum.ENDED;
import static com.griep.postmortem.domain.enums.EventTypeEnum.*;
import static java.lang.Math.*;
import static java.time.DayOfWeek.MONDAY;
import static java.time.Duration.between;
import static java.time.LocalDateTime.now;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class MetricsUtils {
    public static Integer calculateAverage(final Collection<Integer> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        long sum = 0;
        for (Integer x : values) {
            sum += x;
        }
        return (int) floor((double) sum / values.size());
    }

    public static Integer percentile(final List<Integer> sortedAsc, final double percent) {
        if (sortedAsc.isEmpty()) {
            return null;
        }
        int n = sortedAsc.size();
        int idx = (int) ceil(percent * n) - 1;
        if (idx < 0) {
            idx = 0;
        }
        if (idx >= n) {
            idx = n - 1;
        }
        return sortedAsc.get(idx);
    }

    // ===== helpers =====

    public static Double pct(final int num, final int den) {
        if (den <= 0) {
            return null;
        }
        return roundPct((double) num * 100.0 / den);
    }

    public static Double roundPct(final double value) {
        return round(value * 10.0) / 10.0; // 1 casa
    }

//    public static Set<String> lowerSet(final Collection<String> items) {
//        return items.stream()
//                .filter(Objects::nonNull)
//                .map(String::toLowerCase)
//                .collect(toSet());
//    }
//
    public static String nullSafe(final String stringValue) {
        return stringValue == null ?
                "UNSPECIFIED" :
                stringValue;
    }

    public static LocalDate anchorDate(final Incident incident, final DataFieldEnum dateField) {
        if (ENDED.equals(dateField)) {
            return incident.getEndedAt() == null ?
                    null :
                    incident.getEndedAt().toLocalDate();
        }
        return incident.getStartedAt() == null ?
                null :
                incident.getStartedAt().toLocalDate();
    }

    public static LocalDate bucketKey(final LocalDate date, final BucketEnum bucket) {
        if (WEEK.equals(bucket)) {
            // Segunda-feira como início da semana (ISO)
            return date.with(previousOrSame(MONDAY));
        } else if (MONTH.equals(bucket)) {
            return date.withDayOfMonth(1);
        }
        return date; // day
    }

    public static Times computeTimes(final Incident incident,
                               final Collection<IncidentEvent> events,
                               final boolean includeOpenInMttr) {
        LocalDateTime t0 = incident.getStartedAt();
        if (t0 == null) {
            t0 = events.stream()
                    .map(IncidentEvent::getEventAt)
                    .findFirst()
                    .orElse(null);
        }
        if (t0 == null) {
            return new Times(null, null);
        }

        LocalDateTime tack = firstEventAt(events, EnumSet.of(DIAGNOSIS, MITIGATION, FIX));
        LocalDateTime tres = firstEventAt(events, EnumSet.of(FIX));

        var mtta = tack == null ?
                null :
                (int) between(t0, tack).toMinutes();

        Integer mttr;

        if (tres != null) {
            mttr = (int) between(t0, tres).toMinutes();
        } else if (includeOpenInMttr) {
            mttr = (int) between(t0, now()).toMinutes();
        } else {
            mttr = null;
        }

        return new Times(mtta, mttr);
    }

    public static LocalDateTime firstEventAt(final Collection<IncidentEvent> incidentEvents,
                                             final EnumSet<EventTypeEnum> types) {
        for (IncidentEvent incidentEvent : incidentEvents) {
            if (types.contains(incidentEvent.getType())) return incidentEvent.getEventAt();
        }
        return null;
    }

    public static Dist dist(final Collection<Integer> mins) {
        if (mins.isEmpty()) {
            return new Dist(null, null, null, 0);
        }
        List<Integer> sorted =
                mins.stream()
                        .sorted()
                        .toList();
        Integer avg = calculateAverage(sorted);
        Integer p50 = percentile(sorted, 0.5);
        Integer p90 = percentile(sorted, 0.9);
        return new Dist(avg, p50, p90, sorted.size());
    }
}
