package com.epam.rd.autocode.assessment.appliancestore.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        log.error("An unexpected error occurred internals: ", ex);

        return ResponseEntity.internalServerError().body(
                "Unexpected error occurred. Please contact support or try again later.");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.warn("Resource query failed: {}", ex.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        Map<String, String> errorReport = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorReport.put(error.getField(), error.getDefaultMessage()));

        log.warn("Validation constraint violation: {}", errorReport);
        return ResponseEntity.badRequest().body(errorReport);
    }
}
