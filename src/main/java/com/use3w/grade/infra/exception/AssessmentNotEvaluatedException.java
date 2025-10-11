package com.use3w.grade.infra.exception;

public class AssessmentNotEvaluatedException extends RuntimeException {
    public AssessmentNotEvaluatedException(String message) {
        super(message);
    }
}
