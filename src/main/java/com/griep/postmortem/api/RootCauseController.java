package com.griep.postmortem.api;

import com.griep.postmortem.domain.dto.request.RootCauseDTO;
import com.griep.postmortem.domain.dto.response.RootCauseResponseDTO;
import com.griep.postmortem.service.IRootCauseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Validated
@RequestMapping("/api/incidents/{incidentId}/analysis")
@RequiredArgsConstructor
public class RootCauseController {
    private final IRootCauseService service;

    @Operation(summary = "Get the Root Cause", description = "Returns the root cause analysis of incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping
    public ResponseEntity<Optional<RootCauseResponseDTO>> get(
            @PathVariable("incidentId") @Valid @NotNull @Positive final Long incidentId
    ) {
        return ok(service.get(incidentId));
    }

    @Operation(summary = "Create the Root Cause", description = "Create the root cause analysis of incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "422", description = "Business validation")
    })
    @PostMapping
    public ResponseEntity<Void> create(
            @PathVariable("incidentId") @Valid @NotNull @Positive final Long incidentId,
            @RequestBody @Valid @NotNull final RootCauseDTO rootCause
    ) {

        service.create(incidentId, rootCause);
        var location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .buildAndExpand(incidentId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Update the Root Cause", description = "Update the root cause analysis of incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "422", description = "Business validation")
    })
    @PutMapping
    public ResponseEntity<RootCauseResponseDTO> update(
            @PathVariable("incidentId") @Valid @NotNull @Positive final Long incidentId,
            @RequestBody @Valid @NotNull final RootCauseDTO rootCause
    ) {
        return ok(service.update(incidentId, rootCause));
    }
}
