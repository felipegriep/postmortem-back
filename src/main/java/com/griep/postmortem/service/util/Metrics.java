package com.griep.postmortem.service.util;

import java.time.Duration;

public record Metrics(
        Duration mtta,
        Duration mttr,
        Duration duration,
        boolean hasComm,
        long eventCount) {
}
