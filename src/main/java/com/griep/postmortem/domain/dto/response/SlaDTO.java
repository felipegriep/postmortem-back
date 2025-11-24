package com.griep.postmortem.domain.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SlaDTO {
    private boolean overdue;
    private Boolean finishedOnTime;
}
