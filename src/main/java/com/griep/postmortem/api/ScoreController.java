package com.griep.postmortem.api;


import com.griep.postmortem.domain.dto.response.ScoreDTO;
import com.griep.postmortem.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/incidents/{incidentId}/scores")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService service;

    @Operation(summary = "Delete a Event of Incident", description = "Delete a simple event of incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping
    public ResponseEntity<ScoreDTO> delete(
            @PathVariable("incidentId") @Valid @NotNull @Positive final Long incidentId
    ) {
        return ok(service.compute(incidentId));
    }

}
