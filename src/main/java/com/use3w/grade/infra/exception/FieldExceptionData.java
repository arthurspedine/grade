package com.use3w.grade.infra.exception;

import org.springframework.validation.FieldError;

public record FieldExceptionData(
        String field,
        String message
) {
    public FieldExceptionData(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }
}
