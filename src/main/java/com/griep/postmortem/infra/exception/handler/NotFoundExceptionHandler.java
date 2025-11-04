package com.griep.postmortem.infra.exception.handler;

import com.griep.postmortem.infra.exception.NotFoundException;
import com.griep.postmortem.infra.exception.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
@Order(0)
@Slf4j
public class NotFoundExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFound(final NotFoundException exception) {
        log.error("Not found exception: {}", exception.getMessage(), exception);
        ErrorDTO error = new ErrorDTO(NOT_FOUND.value(), exception.getMessage());
        return status(NOT_FOUND).body(error);
    }
}
