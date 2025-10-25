package com.griep.postmortem.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime generatedAt;
    private Short completenessScore;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
