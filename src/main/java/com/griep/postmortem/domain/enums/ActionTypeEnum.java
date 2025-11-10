package com.griep.postmortem.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ActionTypeEnum {
    CORRECTIVE("Corretivo"),
    PREVENTIVE("Preventivo");

    private final String description;
}
