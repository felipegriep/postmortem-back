package com.griep.postmortem.domain.dto.response;

import com.griep.postmortem.domain.enums.EventTypeEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

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
