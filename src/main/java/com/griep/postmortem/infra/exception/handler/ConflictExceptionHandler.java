package com.griep.postmortem.infra.exception.handler;

import com.griep.postmortem.infra.exception.ConflictException;
import com.griep.postmortem.infra.exception.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.CONFLICT;

@ControllerAdvice
@Order(2)
@Slf4j
public class ConflictExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorDTO> handleConflict(final ConflictException exception) {
        log.error("Conflict exception: {}", exception.getMessage(), exception);
        var error = new ErrorDTO(CONFLICT.value(), exception.getMessage());
        return ResponseEntity.status(CONFLICT).body(error);
    }
}
