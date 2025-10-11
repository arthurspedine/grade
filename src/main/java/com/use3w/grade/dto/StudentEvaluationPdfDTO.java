package com.use3w.grade.dto;

import java.util.List;

public record StudentEvaluationPdfDTO(
        StudentPdfInfoDTO student,
        List<AssessmentAnswerDTO> answers,
        String finalFeedback,
        String finishedDate,
        Double totalScore
) {
    public record StudentPdfInfoDTO(
            String name,
            String rm,
            String assessmentName,
            String className
    ) {
    }
}