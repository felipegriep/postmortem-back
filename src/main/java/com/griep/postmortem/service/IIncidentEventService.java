package com.griep.postmortem.service;

import java.time.LocalDateTime;

public interface IIncidentEventService {
    Integer calculateMtta(final Long incidentId, final LocalDateTime startAt);
}
