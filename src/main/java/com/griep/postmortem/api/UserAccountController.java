package com.griep.postmortem.api;

import com.griep.postmortem.domain.dto.request.UserAcoountDTO;
import com.griep.postmortem.domain.dto.response.UserAccountResponseDTO;
import com.griep.postmortem.domain.enums.ProviderEnum;
import com.griep.postmortem.service.IUserAccountService;
import com.griep.postmortem.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

import static java.util.Arrays.stream;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Validated
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAccountController {

    private final IUserAccountService service;

    @Operation(summary = "List of Users", description = "Returns a list, or filtered list, of users account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping
    public ResponseEntity<Page<UserAccountResponseDTO>> list(
            @RequestParam(name = "name", required = false) final String name,
            @RequestParam(name = "email", required = false) final String email,
            @RequestParam(name = "active", required = false) final Boolean active,
            @RequestParam(name = "pageable", required = false, defaultValue = "0") final Pageable pageable
    ) {
        return ok(service.list(name, email, active, pageable));
    }

    @Operation(summary = "Get a User", description = "Returns a simple user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserAccountResponseDTO> get(
            @PathVariable("id") @Valid @NotNull @Positive final Long id
    ) {

        return ok(service.get(id));
    }

    @Operation(summary = "Create a User Account", description = "Create a simple user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "422", description = "Business validation")
    })
    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Valid @NotNull final UserAcoountDTO incident
    ) {

        var id = service.create(incident);
        var location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Update a User Account", description = "Update a simple user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "422", description = "Business validation")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserAccountResponseDTO> update(
            @PathVariable("id") @Valid @Positive final Long id,
            @RequestBody @NotNull @Valid final UserAcoountDTO userAccount
    ) {
        return ok(service.update(id, userAccount));
    }

    @Operation(summary = "List Providers", description = "List all providers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @GetMapping("/providers")
    public ResponseEntity<List<ProviderEnum>> listProviders() {
        return ok(stream(ProviderEnum.values()).toList());
    }
}
