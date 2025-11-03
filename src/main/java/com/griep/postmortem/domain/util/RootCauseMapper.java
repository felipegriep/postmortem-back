package com.griep.postmortem.domain.util;

import com.griep.postmortem.domain.dto.request.RootCauseDTO;
import com.griep.postmortem.domain.dto.response.RootCauseResponseDTO;
import com.griep.postmortem.domain.model.Incident;
import com.griep.postmortem.domain.model.RootCause;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class RootCauseMapper {

    private final static ModelMapper mapper;

    static {
        mapper = new ModelMapper();
    }

    public static RootCauseResponseDTO toDTO(final RootCause rootCause) {
        return mapper.map(rootCause, RootCauseResponseDTO.class);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static Optional<RootCauseResponseDTO> toDTO(final Optional<RootCause> rootCause) {
        return rootCause.map(RootCauseMapper::toDTO);
    }

    public static RootCause toEntity(final Incident incident, final RootCause rootCause, final RootCauseDTO dto) {
        return rootCause.toBuilder()
                .incident(incident)
                .why1(dto.why1())
                .why2(dto.why2())
                .why3(dto.why3())
                .why4(dto.why4())
                .why5(dto.why5())
                .rootCauseText(dto.rootCauseText())
                .contributingFactors(dto.contributingFactors())
                .lessonsLearned(dto.lessonsLearned())
                .build();
    }
}
