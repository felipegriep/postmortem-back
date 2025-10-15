package com.griep.postmortem.domain.dto.response;

import com.griep.postmortem.domain.enums.SeverityEnum;
import com.griep.postmortem.domain.enums.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentResponseDTO {

    private Long id;
    private String title;
    private String service;

    @Enumerated(EnumType.STRING)
    private SeverityEnum severity;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String impactShort;
    private UserAccountResponseDTO reporter;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
