package com.griep.postmortem.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.griep.postmortem.infra.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    OPEN("Open", "Aberto"),
    IN_ANALYSIS("In Analysis", "Em análise"),
    CLOSED("Closed", "Fechado");

    @JsonValue
    private final String value;

    private final String description;

    public static StatusEnum fromValue(final String value) {
        return Arrays.stream(StatusEnum.values())
                .filter(item -> item.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Status not found!"));
    }

}
