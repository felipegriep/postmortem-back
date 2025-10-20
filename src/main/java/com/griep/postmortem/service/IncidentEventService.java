package com.griep.postmortem.service;

import com.griep.postmortem.repository.IncidentEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.time.Duration.between;

@Service
@RequiredArgsConstructor
public class IncidentEventService implements IIncidentEventService {
    private final IncidentEventRepository repository;

    @Override
    public Integer calculateMtta(final Long incidentId, final LocalDateTime startAt) {
        if (startAt == null) {
            return null;
        }

        var firstAlert = repository.firstAlertAt(incidentId);
        if (firstAlert.isEmpty()) {
            return null;
        }

        var minutes = between(startAt, firstAlert.get()).toMinutes();

        return minutes >= 0 ? (int) minutes : null;
    }
}
