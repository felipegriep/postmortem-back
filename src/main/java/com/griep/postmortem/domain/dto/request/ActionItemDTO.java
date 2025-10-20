package com.griep.postmortem.domain.dto.request;

import com.griep.postmortem.domain.enums.ActionStatusEnum;
import com.griep.postmortem.domain.enums.ActionTypeEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionItemDTO {

    @Enumerated(EnumType.STRING)
    @NotNull
    private ActionTypeEnum type;

    @NotNull
    private String description;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ActionStatusEnum status;

    private String evidenceLink;

    private LocalDateTime completedAt;
}
