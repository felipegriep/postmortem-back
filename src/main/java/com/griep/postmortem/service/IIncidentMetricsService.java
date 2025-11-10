package com.griep.postmortem.service;

import java.time.Duration;
import java.time.LocalDateTime;

public interface IIncidentMetricsService {
    Duration[] calculateMttaAndMttr(final Long incidentId, final LocalDateTime startedAt);
}
