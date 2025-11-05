package com.griep.postmortem.domain.util;

import com.griep.postmortem.domain.dto.request.IncidentDTO;
import com.griep.postmortem.domain.dto.response.IncidentResponseDTO;
import com.griep.postmortem.domain.model.Incident;
import com.griep.postmortem.domain.model.UserAccount;
import org.modelmapper.ModelMapper;

public class IncidentMapper {

    private final static ModelMapper mapper;

    static {
        mapper = new ModelMapper();
    }

    public static IncidentResponseDTO toDTOWithMttaAndMttrAndScore(final Incident incident, final Integer mttaMinutes, final Integer score) {
        return mapper.map(incident, IncidentResponseDTO.class).toBuilder()
                .mttaMinutes(mttaMinutes)
                .completenessScore(score)
                .build()
                .calculateMttr();
    }

    public static Incident toEntity(final Incident incident, final IncidentDTO dto, final UserAccount userAccount) {
        return incident.toBuilder()
                .title(dto.getTitle())
                .service(dto.getService())
                .severity(dto.getSeverity())
                .status(dto.getStatus())
                .startedAt(dto.getStartedAt())
                .endedAt(dto.getEndedAt())
                .impactShort(dto.getImpactShort())
                .reporter(userAccount)
                .build();
    }
}
