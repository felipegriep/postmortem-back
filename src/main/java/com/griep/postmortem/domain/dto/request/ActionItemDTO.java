package com.griep.postmortem.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.griep.postmortem.domain.enums.ActionStatusEnum;
import com.griep.postmortem.domain.enums.ActionTypeEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record ActionItemDTO(
        @Enumerated(EnumType.STRING) @NotNull ActionTypeEnum type,
        @NotNull String description,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo") LocalDateTime dueDate,
        @Enumerated(EnumType.STRING) @NotNull ActionStatusEnum status,
        String evidenceLink,
        @Positive Long ownerId) {
}
