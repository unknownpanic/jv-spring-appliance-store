package com.epam.rd.autocode.assessment.appliancestore.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        return ResponseEntity.internalServerError().body(
                "Unexpected error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        Map<String, String> errorReport = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorReport.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errorReport);
    }
}
