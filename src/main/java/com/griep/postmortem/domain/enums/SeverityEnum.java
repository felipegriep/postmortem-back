package com.griep.postmortem.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.griep.postmortem.infra.exception.NotFoundException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SeverityEnum {
    SEV_1("SEV-1"),
    SEV_2("SEV-2"),
    SEV_3("SEV-3"),
    SEV_4("SEV-4");

    @JsonValue
    private final String value;

    SeverityEnum(final String value) {
        this.value = value;
    }

    public static SeverityEnum fromValue(final String value) {
        return Arrays.stream(SeverityEnum.values())
                .filter(item -> item.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Severity not found!"));
    }
}
