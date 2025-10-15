package com.griep.postmortem.domain.dto.request;

import com.griep.postmortem.domain.enums.ProviderEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginDTO {

    @NotBlank
    private String idToken;
    @NotNull
    private ProviderEnum provider;
}
