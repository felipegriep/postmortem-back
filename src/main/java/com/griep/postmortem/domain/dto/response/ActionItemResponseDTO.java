package com.griep.postmortem.domain.dto.response;

import com.griep.postmortem.domain.enums.ActionStatusEnum;
import com.griep.postmortem.domain.enums.ActionTypeEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionItemResponseDTO {

    private Long id;
    private Long incidentId;

    @Enumerated(EnumType.STRING)
    private ActionTypeEnum type;

    private String description;
    private UserAccountResponseDTO owner;
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private ActionStatusEnum status;

    private String evidenceLink;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
