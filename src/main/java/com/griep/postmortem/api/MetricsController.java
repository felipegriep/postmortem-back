package com.griep.postmortem.api;

import com.griep.postmortem.domain.dto.response.MetricsSeriesDTO;
import com.griep.postmortem.domain.dto.response.MetricsSummaryDTO;
import com.griep.postmortem.domain.enums.BucketEnum;
import com.griep.postmortem.domain.enums.DataFieldEnum;
import com.griep.postmortem.domain.enums.SeverityEnum;
import com.griep.postmortem.domain.enums.StatusEnum;
import com.griep.postmortem.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService service;

    @GetMapping("/summary")
    public ResponseEntity<MetricsSummaryDTO> summary(
            @RequestParam @DateTimeFormat(iso = DATE) final LocalDate from,
            @RequestParam @DateTimeFormat(iso = DATE) final LocalDate to,
            @RequestParam(defaultValue = "STARTED") final DataFieldEnum dateField,  // STARTED || ENDED
            @RequestParam(required = false) List<SeverityEnum> severities,          // ex: SEV-1, SEV-2
            @RequestParam(required = false) List<StatusEnum> statuses,              // ex: OPEN, CLOSED
            @RequestParam(defaultValue = "false") boolean includeOpenInMttr         // inclui abertos no MTTR?
    ) {
        return ok(service.summary(from, to, dateField, severities, statuses, includeOpenInMttr));
    }

    @GetMapping("/series")
    public ResponseEntity<MetricsSeriesDTO> series(
            @RequestParam @DateTimeFormat(iso = DATE) final LocalDate from,
            @RequestParam @DateTimeFormat(iso = DATE) final LocalDate to,
            @RequestParam(defaultValue = "DAY") final BucketEnum bucket,            // DAY || WEEK || MONTH
            @RequestParam(defaultValue = "STARTED") final DataFieldEnum dateField,  // STARTED || ENDED
            @RequestParam(required = false) List<SeverityEnum> severities
    ) {
        return ok(service.series(from, to, bucket, dateField, severities));
    }
}