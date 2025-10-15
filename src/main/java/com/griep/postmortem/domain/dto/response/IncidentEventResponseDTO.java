package com.griep.postmortem.domain.dto.response;

import com.griep.postmortem.domain.enums.EventTypeEnum;
import com.griep.postmortem.domain.model.Incident;
import com.griep.postmortem.domain.model.UserAccount;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentEventResponseDTO {

    private Long id;
    private Long incidentId;
    private LocalDateTime eventAt;

    @Enumerated(EnumType.STRING)
    private EventTypeEnum type;

    private String description;
    private UserAccountResponseDTO actor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
