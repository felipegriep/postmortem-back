package com.griep.postmortem.domain.dto.request;

import com.griep.postmortem.domain.enums.EventTypeEnum;
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
public class IncidentEventDTO {

    @NotNull
    private LocalDateTime eventAt;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EventTypeEnum type;

    @NotNull
    private String description;
}
