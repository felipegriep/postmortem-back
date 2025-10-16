package com.griep.postmortem.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.griep.postmortem.infra.exception.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum StatusEnum {
    OPEN("Open"),
    IN_ANALYSIS("In Analysis"),
    CLOSED("Closed");

    @JsonValue
    private final String value;

    public static StatusEnum fromValue(final String value) {
        return Arrays.stream(StatusEnum.values())
                .filter(item -> item.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Status not found!"));
    }

}
