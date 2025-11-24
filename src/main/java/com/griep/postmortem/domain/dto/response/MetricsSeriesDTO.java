package com.griep.postmortem.domain.dto.response;

import com.griep.postmortem.domain.enums.BucketEnum;

import java.util.List;

public record MetricsSeriesDTO(
        BucketEnum bucket,  // "day" | "week" | "month"
        List<Point> points
) {
    public record Point(
            String at,      // início do bucket (yyyy-MM-dd)
            int incidents,
            Integer avgMttaMin,
            Integer avgMttrMin,
            Integer avgScore,
            Double onTimeRatePct
    ) {
    }
}