package com.use3w.grade.dto;

import java.util.Set;
import java.util.UUID;

public record AssessmentDetailsDTO(
        UUID id,
        String name,
        String assessmentDate,
        Set<AssessmentDetailsClassDTO> classes
) {
    public record AssessmentDetailsClassDTO(
            UUID id, String name, Integer countEvaluatedStudents, Integer countStudents
    ) {}
}
