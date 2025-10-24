package com.griep.postmortem.domain.dto.response;

import java.time.LocalDateTime;

public record ScoreDTO(
        Long incidentId,
        int score,
        Breakdown breakdown,
        Checks checks,
        LocalDateTime evaluatedAt
) {
    public record Breakdown(
            int timeline,
            int impact,
            int whys,
            int rootAndFactors,
            int actions,
            int communication
    ) {}

    public record Checks(
            boolean hasMinEvents,
            boolean hasImpact,
            boolean hasFiveWhys,
            boolean hasRootAndFactors,
            boolean hasCorrectiveAndPreventiveWithOwnerAndDue,
            boolean hasCommunication
    ) {}
}
