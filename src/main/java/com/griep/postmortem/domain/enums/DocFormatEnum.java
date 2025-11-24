package com.griep.postmortem.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DocFormatEnum {
    MD("text/markdown; charset=UTF-8", ".md"),
    HTML("text/html; charset=UTF-8", ".html"),
    PDF("application/pdf; charset=UTF-8", ".pdf");

    private final String mimeType;
    private final String extension;
}
