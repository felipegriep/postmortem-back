package com.griep.postmortem.api;

import com.griep.postmortem.domain.dto.request.ActionItemDTO;
import com.griep.postmortem.domain.dto.response.ActionItemResponseDTO;
import com.griep.postmortem.domain.enums.ActionStatusEnum;
import com.griep.postmortem.domain.enums.ActionTypeEnum;
import com.griep.postmortem.service.IActionItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.fromString;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/incidents/{incidentId}/actions")
@Validated
public class ActionItemController {
    private final IActionItemService service;

    @Operation(summary = "List of Action Items of Incidents", description = "Returns a list of action items of incidents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping
    public Page<ActionItemResponseDTO> list(
            @PathVariable @Valid @NotNull @Positive final long incidentId,
            @RequestParam(name = "actionType", required = false) final ActionTypeEnum actionType,
            @RequestParam(name = "actionStatus", required = false) final ActionStatusEnum actionStatus,
            @RequestParam(name = "ownerId", required = false) @Valid @Positive final Long ownerId,
            @RequestParam(name = "overdue", required = false) final Boolean overdue,
            @RequestParam(name = "query", required = false) final String query,
            @RequestParam(name = "page", required = false, defaultValue = "0") final Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") final Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "dueDate") final String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") final String direction) {
        return service.list(
                incidentId,
                actionType,
                actionStatus,
                ownerId,
                overdue,
                query,
                of(page, size, by(fromString(direction), sort)));
    }

    @Operation(summary = "Create a Action Item of Incident", description = "Create a simple action item of incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PostMapping
    public ResponseEntity<Void> create(
            @PathVariable("incidentId") @Valid @NotNull @Positive final Long incidentId,
            @Valid @RequestBody @NotNull final ActionItemDTO actionItem,
            @AuthenticationPrincipal final String userEmail
    ) {
        service.create(incidentId, actionItem, userEmail);

        return status(CREATED).build();
    }

    @Operation(summary = "Update a Action Item of Incident", description = "Update a simple action item of incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "422", description = "Business validation")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ActionItemResponseDTO> update(
            @PathVariable("incidentId") @Valid @NotNull @Positive final Long incidentId,
            @PathVariable("id") @Valid @NotNull @Positive final Long id,
            @Valid @RequestBody @NotNull final ActionItemDTO actionItem,
            @AuthenticationPrincipal final String userEmail
    ) {
        return ok(service.update(incidentId, id, actionItem, userEmail));
    }

    @Operation(summary = "Delete a Action Item of Incident", description = "Delete a simple action item of incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("incidentId") @Valid @NotNull @Positive final Long incidentId,
            @PathVariable("id") @Valid @Positive final Long id
    ) {
        service.delete(incidentId, id);
        return noContent().build();
    }

}
