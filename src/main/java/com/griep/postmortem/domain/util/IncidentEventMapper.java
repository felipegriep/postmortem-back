package com.griep.postmortem.domain.util;

import com.griep.postmortem.domain.dto.request.IncidentEventDTO;
import com.griep.postmortem.domain.dto.response.IncidentEventResponseDTO;
import com.griep.postmortem.domain.model.Incident;
import com.griep.postmortem.domain.model.IncidentEvent;
import org.modelmapper.ModelMapper;

public class IncidentEventMapper {

    private final static ModelMapper mappeer;

    static {
        mappeer = new ModelMapper();
    }

    public static IncidentEventResponseDTO toDTO(final IncidentEvent incident) {
        return mappeer.map(incident, IncidentEventResponseDTO.class);
    }

    public static IncidentEvent toEntity(final Incident incident, final IncidentEvent incidentEvent, final IncidentEventDTO dto) {
        return incidentEvent.toBuilder()
                .incident(incident)
                .eventAt(incidentEvent.getId() == null ? dto.getEventAt() : incidentEvent.getEventAt())
                .type(incidentEvent.getId() == null ? dto.getType() : incidentEvent.getType())
                .description(dto.getDescription())
                .actor(null)
                .build();
    }
}
