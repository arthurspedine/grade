package com.use3w.grade.dto;

import java.util.List;

public record EditAssessmentDTO(
        String name,
        List<ClassToAssessmentDTO> classes,
        List<AssessmentQuestionDTO> questions,
        String assessmentDate
) {
}
