package com.griep.postmortem.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.griep.postmortem.infra.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SeverityEnum {
    SEV_1("SEV-1", "Crítica"),
    SEV_2("SEV-2", "Alta"),
    SEV_3("SEV-3", "Moderada"),
    SEV_4("SEV-4", "Baixa");

    @JsonValue
    private final String value;

    private String description;

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
