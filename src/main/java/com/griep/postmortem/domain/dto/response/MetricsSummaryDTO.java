package com.griep.postmortem.domain.dto.response;

import com.griep.postmortem.domain.enums.DataFieldEnum;
import com.griep.postmortem.domain.enums.SeverityEnum;
import com.griep.postmortem.domain.enums.StatusEnum;

import java.util.List;
import java.util.Map;

public record MetricsSummaryDTO(
        Window window,
        Filters filters,
        IncidentsKpi incidents,
        TimeKpi time,
        ScoreKpi score,
        ActionsKpi actions
) {
    public record Window(
            String from,
            String to,
            DataFieldEnum dateField
    ) {
    }

    public record Filters(
            List<SeverityEnum> severities,
            List<StatusEnum> statuses
    ) {
    }

    public record IncidentsKpi(
            int total,
            int open,
            int closed,
            Map<String, Integer> bySeverity
    ) {
    }

    public record TimeKpi(
            Dist mtta,
            Dist mttr
    ) {
        public record Dist(
                Integer avgMin,
                Integer p50Min,
                Integer p90Min,
                int count
        ) {
        }
    }

    public record ScoreKpi(
            Integer avg,
            Integer p50,
            Integer p90,
            int count,
            ChecksCoverage checksCoveragePct
    ) {
        public record ChecksCoverage(
                Double minEvents,
                Double impact,
                Double fiveWhys,
                Double rootAndFactors,
                Double actionsBoth,
                Double communication
        ) {
        }
    }

    public record ActionsKpi(
            int total,
            int open,
            int overdueOpen,
            int done,
            int doneOnTime,
            int doneLate,
            Double onTimeRatePct
    ) {
    }
}