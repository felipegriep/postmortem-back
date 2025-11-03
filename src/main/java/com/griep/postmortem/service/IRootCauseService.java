package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.RootCauseDTO;
import com.griep.postmortem.domain.dto.response.RootCauseResponseDTO;

import java.util.Optional;

public interface IRootCauseService {
    void create(final Long incidentId, final RootCauseDTO rootCause);
    RootCauseResponseDTO update(final Long incidentId, final RootCauseDTO rootCause);
    Optional<RootCauseResponseDTO> get(final Long incidentId);
}
