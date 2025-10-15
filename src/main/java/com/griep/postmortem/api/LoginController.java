package com.griep.postmortem.api;


import com.griep.postmortem.domain.dto.request.AuthLoginDTO;
import com.griep.postmortem.domain.dto.response.AuthLoginResponseDTO;
import com.griep.postmortem.service.ILoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Validated
@RequestMapping("/auth/login")
@RequiredArgsConstructor
public class LoginController {

    private final ILoginService loginService;

    @Operation(summary = "Login", description = "Realiza o login e retorna o token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<AuthLoginResponseDTO> login(
            @RequestBody @Valid @NotNull final AuthLoginDTO login
    ) {
        return ok(loginService.login(login));
    }
}
