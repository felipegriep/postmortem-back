package com.griep.postmortem.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostmortemDocResponseDTO {

    private Long id;
    private Long incidentId;
    private String mdContent;
    private LocalDateTime generatedAt;
    private Short completenessScore;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
