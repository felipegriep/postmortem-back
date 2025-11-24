package com.griep.postmortem.infra.exception.handler;

import com.griep.postmortem.infra.exception.BusinessValidationException;
import com.griep.postmortem.infra.exception.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
@Order(1)
@Slf4j
public class BusinessValidationExceptionHandler {

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorDTO> handleBusinessValidationException(final BusinessValidationException exception) {
        log.error("Business validation exception: {}", exception.getMessage(), exception);
        var error = new ErrorDTO(UNPROCESSABLE_ENTITY.value(), exception.getMessage());
        return status(UNPROCESSABLE_ENTITY).body(error);
    }
}
