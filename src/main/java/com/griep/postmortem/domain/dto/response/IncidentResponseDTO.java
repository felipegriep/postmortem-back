package com.griep.postmortem.domain.dto.response;

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

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String impactShort;
    private UserAccountResponseDTO reporter;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer mttaMinutes;
    private Integer mttrMinutes;

    public IncidentResponseDTO calculateMttr() {
        if (this.startedAt == null || this.endedAt == null) {
            return this;
        }

        var minutes = between(this.startedAt, this.endedAt).toMinutes();
        this.mttrMinutes = minutes >= 0 ? (int) minutes : null;
        return this;
    }
}
