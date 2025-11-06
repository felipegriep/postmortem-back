package com.griep.postmortem.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DocFormatEnum {
    MD("text/markdown; charset=UTF-8", ".md"),
    HTML("text/html; charset=UTF-8", ".html");

    private final String mimeType;
    private final String extension;
}
