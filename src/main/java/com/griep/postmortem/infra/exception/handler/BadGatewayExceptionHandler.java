package com.griep.postmortem.infra.exception.handler;

import com.griep.postmortem.infra.exception.BadGatewayException;
import com.griep.postmortem.infra.exception.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@ControllerAdvice
@Order(0)
@Slf4j
public class BadGatewayExceptionHandler {

    @ExceptionHandler(BadGatewayException.class)
    public ResponseEntity<ErrorDTO> handleBadGatewayException(final BadGatewayException exception) {
        log.error("BadGatewayException: {}", exception.getMessage(), exception);
        return ResponseEntity.status(BAD_GATEWAY)
                .body(new ErrorDTO(
                        BAD_GATEWAY.value(),
                        exception.getMessage()));
    }
}
