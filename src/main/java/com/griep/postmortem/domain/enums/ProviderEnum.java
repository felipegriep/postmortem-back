package com.griep.postmortem.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProviderEnum {
    LOCAL("Local"),
    GOOGLE("Google"),
    GITHUB("GitHub"),
    SAML("Saml");

    @JsonValue
    private final String value;
}
