package com.griep.postmortem.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ActionStatusEnum {
    TODO("À fazer"),
    DOING("Fazendo"),
    DONE( "Feito"),
    LATE("Atrasado");

    private final String description;
}
