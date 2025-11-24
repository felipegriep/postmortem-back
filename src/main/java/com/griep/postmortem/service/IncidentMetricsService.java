package com.griep.postmortem.service;

import com.griep.postmortem.domain.model.IncidentEvent;
import com.griep.postmortem.repository.IncidentEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

import static com.griep.postmortem.domain.enums.EventTypeEnum.*;
import static java.time.Duration.between;

@Component
@RequiredArgsConstructor
public class IncidentMetricsService implements IIncidentMetricsService {
    private final IncidentEventRepository repository;

    public Duration[] calculateMttaAndMttr(final Long incidentId, final LocalDateTime startedAt) {
        var metrics = new Duration[] {null, null};

        var incidentEvents = repository.findByIncidentIdOrderByEventAtAsc(incidentId);
        var first = incidentEvents.stream()
                .findFirst()
                .map(IncidentEvent::getEventAt)
                .orElse(null);

        var t0 = startedAt != null ?
                startedAt :
                first;

        var mtta = calculateMtta(incidentEvents, t0);
        var mttr = calculateMttr(incidentEvents, t0);

        metrics[0] = ((mtta == null) || (mtta.isNegative())) ? null : mtta;
        metrics[1] = ((mttr == null) || (mttr.isNegative())) ? null : mttr;

        return metrics;
    }

    private Duration calculateMtta(final Collection<IncidentEvent> incidentEvents, final LocalDateTime t0) {

        var tAlert = incidentEvents.stream()
                .filter(incidentEvent -> incidentEvent.getType() == ALERT)
                .map(IncidentEvent::getEventAt)
                .findFirst()
                .orElse(null);

        LocalDateTime tReference;

        if (tAlert != null && t0 != null && tAlert.isAfter(t0)) {
            tReference = tAlert;
        } else {
            tReference = t0;
        }

        var tAck = incidentEvents.stream()
                .filter(incidentEvent ->
                        incidentEvent.getType() == DIAGNOSIS &&
                                !incidentEvent.getEventAt().isBefore(tReference))
                .map(IncidentEvent::getEventAt)
                .findFirst()
                .orElseGet(() -> incidentEvents.stream()
                        .filter(incidentEvent ->
                                incidentEvent.getType() == MITIGATION &&
                                        !incidentEvent.getEventAt().isBefore(tReference))
                        .map(IncidentEvent::getEventAt)
                        .findFirst()
                        .orElseGet(() -> incidentEvents.stream()
                                .filter(incidentEvent ->
                                        incidentEvent.getType() == FIX &&
                                                !incidentEvent.getEventAt().isBefore(tReference))
                                .map(IncidentEvent::getEventAt)
                                .findFirst()
                                .orElse(null)));

        return (tReference != null && tAck != null) ? between(tReference, tAck) : null;
    }

    private Duration calculateMttr(final Collection<IncidentEvent> incidentEvents, final LocalDateTime t0) {
        var tRes = incidentEvents.stream()
                .filter(incidentEvent -> incidentEvent.getType() == FIX)
                .map(IncidentEvent::getEventAt)
                .findFirst()
                .orElse(null);

        return (t0 != null && tRes != null) ? between(t0, tRes) : null;
    }
}
