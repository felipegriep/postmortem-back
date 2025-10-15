package com.griep.postmortem.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RootCauseDTO {

    @NotNull
    private String why1;

    @NotNull
    private String why2;

    @NotNull
    private String why3;

    @NotNull
    private String why4;

    @NotNull
    private String why5;

    @NotNull
    private String rootCauseText;

    private String contributingFactors;

    private String lessonsLearned;
}
