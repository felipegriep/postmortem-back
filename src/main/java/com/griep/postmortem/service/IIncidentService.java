package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.IncidentDTO;
import com.griep.postmortem.domain.dto.response.IncidentResponseDTO;
import com.griep.postmortem.domain.enums.SeverityEnum;
import com.griep.postmortem.domain.enums.StatusEnum;
import com.griep.postmortem.domain.model.Incident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IIncidentService {
    Page<IncidentResponseDTO> list(final String service, final SeverityEnum severity, final StatusEnum status, final Pageable pageable);
    IncidentResponseDTO get(final Long id);
    Incident getEntity(final Long id);
    Long create(final IncidentDTO incident, final String userEmail);
    IncidentResponseDTO update(final Long id, final IncidentDTO incident, final String userEmail);
    void delete(final Long id);
}
