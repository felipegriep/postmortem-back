package com.griep.postmortem.domain.dto.request;

import com.griep.postmortem.domain.enums.ProviderEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginDTO {

    private String idToken;
    private ProviderEnum provider;
}
