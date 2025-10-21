package com.griep.postmortem.api;

import com.griep.postmortem.domain.dto.request.IncidentDTO;
import com.griep.postmortem.domain.dto.response.IncidentResponseDTO;
import com.griep.postmortem.domain.enums.SeverityEnum;
import com.griep.postmortem.domain.enums.StatusEnum;
import com.griep.postmortem.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

import static java.util.Arrays.stream;
import static org.springframework.data.domain.Pageable.ofSize;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Validated
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService service;

    @Operation(summary = "List of Incidents", description = "Returns a list, or filtered list, of incidents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping
    public ResponseEntity<Page<IncidentResponseDTO>> list(
            @RequestParam(name = "serviceName", required = false) final String serviceName,
            @RequestParam(name = "severity", required = false) final SeverityEnum severity,
            @RequestParam(name = "status", required = false) final StatusEnum status,
            @RequestParam(name = "page", required = false, defaultValue = "0") final Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") final Integer size
            ) {
        return ok(service.list(serviceName, severity, status, ofSize(size).withPage(page)));
    }

    @Operation(summary = "Get a Incident", description = "Returns a simple incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> get(
            @PathVariable("id") @Valid @NotNull @Positive final Long id
    ) {

        return ok(service.get(id));
    }

    @Operation(summary = "Create a Incident", description = "Create a simple incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "422", description = "Business validation")
    })
    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Valid @NotNull final IncidentDTO incident
    ) {

        var id = service.create(incident);
        var location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Update a Incident", description = "Update a simple incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "422", description = "Business validation")
    })
    @PutMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> update(
            @PathVariable("id") @Valid @Positive final Long id,
            @RequestBody @NotNull @Valid final IncidentDTO incident
    ) {
        return ok(service.update(id, incident));
    }

    @Operation(summary = "Delete a Incident", description = "Delete a simple incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") @Valid @Positive final Long id
    ) {
        service.delete(id);
        return noContent().build();
    }

    @Operation(summary = "List Severities", description = "List all severities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @GetMapping("/severities")
    public ResponseEntity<List<SeverityEnum>> listSeverities() {
        return ok(stream(SeverityEnum.values()).toList());
    }

    @Operation(summary = "List Statuses", description = "List all statuses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @GetMapping("/statuses")
    public ResponseEntity<List<StatusEnum>> listStatus() {
        return ok(stream(StatusEnum.values()).toList());
    }



}
