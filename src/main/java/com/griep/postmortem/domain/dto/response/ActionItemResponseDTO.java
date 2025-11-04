package com.griep.postmortem.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.griep.postmortem.domain.enums.ActionStatusEnum;
import com.griep.postmortem.domain.enums.ActionTypeEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

import static com.griep.postmortem.domain.enums.ActionStatusEnum.DONE;
import static java.time.LocalDateTime.now;

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

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private ActionStatusEnum status;

    private String evidenceLink;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime completedAt;

    private SlaDTO sla;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime createdAt;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime updatedAt;

    public ActionItemResponseDTO makeSla() {
        var overdue = now().isAfter(dueDate) &&
                !DONE.equals(this.status);
        Boolean finishedOnTime =
                DONE.equals(this.status) &&
                this.completedAt != null &&
                        !this.completedAt.isAfter(dueDate) ?
                        true :
                        null;

        this.setSla(SlaDTO.builder()
                .overdue(overdue)
                .finishedOnTime(finishedOnTime)
                .build());

        return this;
    }
}
