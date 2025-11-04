package com.griep.postmortem.infra.exception.handler;

import com.griep.postmortem.infra.exception.dto.ErrorDTO;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
@Order(0)
@Slf4j
public class BadRequestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValidException: {}", exception.getMessage(), exception);
        var message = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    var defaultMessage = error.getDefaultMessage();
                    try {
                        var field = error.unwrap(org.springframework.validation.FieldError.class).getField();
                        return field + ": " + defaultMessage;
                    } catch (Exception e) {
                        return defaultMessage;
                    }
                })
                .collect(Collectors.joining("; "));
        return status(BAD_REQUEST).body(new ErrorDTO(BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorDTO> handleBindException(final BindException exception) {
        log.error("BindException: {}", exception.getMessage(), exception);
        var message = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    var defaultMessage = error.getDefaultMessage();
                    try {
                        var field = error.unwrap(org.springframework.validation.FieldError.class).getField();
                        return field + ": " + defaultMessage;
                    } catch (Exception e) {
                        return defaultMessage;
                    }
                })
                .collect(Collectors.joining("; "));
        return status(BAD_REQUEST).body(new ErrorDTO(BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleConstraintViolation(final ConstraintViolationException exception) {
        log.error("ConstraintViolationException: {}", exception.getMessage(), exception);
        var message = exception.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));
        return status(BAD_REQUEST).body(new ErrorDTO(BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handleHttpMessageNotReadable(final HttpMessageNotReadableException exception) {
        log.error("HttpMessageNotReadableException: {}", exception.getMessage(), exception);
        var cause = exception.getMostSpecificCause();
        var message = (cause != null && cause.getMessage() != null) ? cause.getMessage() : "Malformed request body";
        return status(BAD_REQUEST).body(new ErrorDTO(BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException exception) {
        log.error("MethodArgumentTypeMismatchException: {}", exception.getMessage(), exception);
        String param = exception.getName();
        String required = exception.getRequiredType() != null ? exception.getRequiredType().getSimpleName() : "unknown";
        String value = exception.getValue() != null ? exception.getValue().toString() : "null";
        var message = "Parâmetro '" + param + "' inválido. Esperado: " + required + ", recebido: " + value;
        return status(BAD_REQUEST).body(new ErrorDTO(BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDTO> handleMissingServletRequestParameter(final MissingServletRequestParameterException exception) {
        log.error("MissingServletRequestParameterException: {}", exception.getMessage(), exception);
        var message = "Parâmetro obrigatório ausente: '" + exception.getParameterName() + "' (tipo: " + exception.getParameterType() + ")";
        return status(BAD_REQUEST).body(new ErrorDTO(BAD_REQUEST.value(), message));
    }
}
