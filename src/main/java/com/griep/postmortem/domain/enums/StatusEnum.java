package com.griep.postmortem.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusEnum {
    OPEN("Open"),
    IN_ANALYSIS("In Analysis"),
    CLOSED("Closed");

    @JsonValue
    private final String value;
}
