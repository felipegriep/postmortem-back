package com.griep.postmortem.domain.dto.request;

import jakarta.validation.constraints.NotNull;

public record RootCauseDTO(
        @NotNull
        String why1,
        @NotNull
        String why2,
        @NotNull
        String why3,
        @NotNull
        String why4,
        @NotNull
        String why5,
        @NotNull
        String rootCauseText,
        String contributingFactors,
        String lessonsLearned) {
}
