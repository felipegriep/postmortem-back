package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.IncidentEventDTO;
import com.griep.postmortem.domain.dto.response.IncidentEventResponseDTO;

import java.util.List;

public interface IIncidentEventService {
    List<IncidentEventResponseDTO> list(final Long incidentId);
    void create(final Long incidentId, final IncidentEventDTO incidentEvent);
    IncidentEventResponseDTO update(final Long incidentId, final Long id, final IncidentEventDTO incidentEvent);
    void delete(final Long incidentId, final Long id);
}
