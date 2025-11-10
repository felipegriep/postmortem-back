package com.griep.postmortem.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EventTypeEnum {
    ALERT( "Alerta"),
    DIAGNOSIS("Diagnóstico"),
    MITIGATION("Mitigação"),
    FIX("Correção"),
    COMMUNICATION("Comunicação");

    private final String description;
}
