package com.griep.postmortem.domain.util;

import com.griep.postmortem.domain.dto.response.PostmortemDocResponseDTO;
import com.griep.postmortem.domain.model.PostmortemDoc;
import org.modelmapper.ModelMapper;

import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PostmortemDocMapper {
    public static PostmortemDocResponseDTO toDTO(final PostmortemDoc postmortemDoc) {
        if (postmortemDoc == null) return null;

        return PostmortemDocResponseDTO.builder()
                .id(postmortemDoc.getId())
                .incidentId(postmortemDoc.getIncident() != null ?
                        postmortemDoc.getIncident().getId() :
                        null)
                .mdContent(postmortemDoc.getMdContent() != null ?
                        postmortemDoc.getMdContent().getBytes(UTF_8) :
                        null)
                .generatedAt(postmortemDoc.getGeneratedAt())
                .completenessScore(postmortemDoc.getCompletenessScore())
                .version(postmortemDoc.getVersion())
                .createdAt(postmortemDoc.getCreatedAt())
                .updatedAt(postmortemDoc.getUpdatedAt())
                .build();
    }
}
