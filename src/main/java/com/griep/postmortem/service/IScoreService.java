package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.response.ScoreDTO;
import com.griep.postmortem.domain.model.Incident;

public interface IScoreService {
    ScoreDTO compute(final Incident incident);
    ScoreDTO compute(final Long incidentId);
}
