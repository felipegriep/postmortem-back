package com.griep.postmortem.api;


import com.griep.postmortem.domain.dto.request.AuthLoginDTO;
import com.griep.postmortem.domain.dto.response.AuthLoginResponseDTO;
import com.griep.postmortem.service.ILoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Authentication")
public class LoginController {

    private final ILoginService loginService;

    @Operation(summary = "Validate external token and issue internal JWT")
    @PostMapping()
    public ResponseEntity<AuthLoginResponseDTO> login(@RequestBody @Valid AuthLoginDTO request) {
        return ok(loginService.login(request));
    }
}
