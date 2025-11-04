package com.griep.postmortem.infra.exception.dto;

public record ErrorDTO(
        Integer statusCode,
        String message
) {
}
