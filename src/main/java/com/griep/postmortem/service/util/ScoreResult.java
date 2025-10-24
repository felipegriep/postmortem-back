package com.griep.postmortem.service.util;

public record ScoreResult(
        int timeline,
        int impact,
        int whys,
        int root,
        int actions,
        int comm,
        int total) {
    public record RootCauseResult(
            boolean hasFiveWhys,
            boolean hasRootAndFactors) {
    }

    public record TimelineResult(
            boolean hasMinEvents,
            boolean hasCommunication) {
    }
}
