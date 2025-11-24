package com.griep.postmortem.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DocDispositionEnum {
    INLINE( "inline"),
    ATTACHMENT( "attachment");

    private final String value;

    public static String valueOf(final DocDispositionEnum docDispositionEnum) {
        return docDispositionEnum.getValue();
    }
}
