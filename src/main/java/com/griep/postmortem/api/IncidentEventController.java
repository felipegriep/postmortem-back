package com.griep.postmortem.api;

import com.griep.postmortem.domain.dto.request.IncidentEventDTO;
import com.griep.postmortem.domain.dto.response.IncidentEventResponseDTO;
import com.griep.postmortem.service.IncidentEventService;
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

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.*;

@RestController
@Validated
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentEventController {

    private final IncidentEventService service;

    @Operation(summary = "List of Events of Incidents", description = "Returns a list of events of incidents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping("/{incidentId}/events")
    public ResponseEntity<List<IncidentEventResponseDTO>> list(
            @PathVariable("incidentId") @Valid @NotNull @Positive final Long incidentId) {
        return ok(service.list(incidentId));
    }

    @Operation(summary = "Create a Event of Incident", description = "Create a simple event of incidentEvent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "422", description = "Business validation")
    })
    @PostMapping("/{incidentId}/events")
    public ResponseEntity<Void> create(
            @PathVariable("incidentId") @Valid @NotNull @Positive final Long incidentId,
            @RequestBody @Valid @NotNull final IncidentEventDTO incidentEvent
    ) {

        service.create(incidentId, incidentEvent);

        return status(CREATED).build();
    }

    @Operation(summary = "Update a Event of Incident", description = "Update a simple event of incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "422", description = "Business validation")
    })
    @PutMapping("/{incidentId}/events/{id}")
    public ResponseEntity<IncidentEventResponseDTO> update(
            @PathVariable("incidentId") @Valid @NotNull @Positive final Long incidentId,
            @PathVariable("id") @Valid @Positive final Long id,
            @RequestBody @NotNull @Valid final IncidentEventDTO incidentEvent
    ) {
        return ok(service.update(incidentId, id, incidentEvent));
    }

    @Operation(summary = "Delete a Event of Incident", description = "Delete a simple event of incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping("/{incidentId}/events/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("incidentId") @Valid @NotNull @Positive final Long incidentId,
            @PathVariable("id") @Valid @Positive final Long id
    ) {
        service.delete(incidentId, id);
        return noContent().build();
    }

}
