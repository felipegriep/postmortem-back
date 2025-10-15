package com.griep.postmortem.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RootCauseResponseDTO {

    private Long id;
    private Long incidentId;
    private String why1;
    private String why2;
    private String why3;
    private String why4;
    private String why5;
    private String rootCauseText;
    private String contributingFactors;
    private String lessonsLearned;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
