package com.use3w.grade.infra.exception;

import java.util.List;

public class StudentCsvValidationException extends RuntimeException {

    private final List<String> validationErrors;

    public StudentCsvValidationException(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
