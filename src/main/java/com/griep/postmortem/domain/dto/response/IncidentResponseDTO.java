package com.griep.postmortem.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.griep.postmortem.domain.enums.SeverityEnum;
import com.griep.postmortem.domain.enums.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

import static java.time.Duration.between;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class IncidentResponseDTO {

    private Long id;
    private String title;
    private String service;

    @Enumerated(EnumType.STRING)
    private SeverityEnum severity;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime startedAt;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime endedAt;
    private String impactShort;
    private UserAccountResponseDTO reporter;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer mttaMinutes;
    private Integer mttrMinutes;
    private Integer completenessScore;
}
