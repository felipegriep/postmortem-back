package com.griep.postmortem.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostmortemDocDTO {

    @NotNull
    private String mdContent;

    @NotNull
    private Short completenessScore;
}
