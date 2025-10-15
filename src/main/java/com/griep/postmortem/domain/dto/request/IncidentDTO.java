package com.griep.postmortem.domain.dto.request;

import com.griep.postmortem.domain.enums.SeverityEnum;
import com.griep.postmortem.domain.enums.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentDTO {

    @NotNull
    private String title;

    @NotNull
    private String service;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SeverityEnum severity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @NotNull
    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private String impactShort;
}
