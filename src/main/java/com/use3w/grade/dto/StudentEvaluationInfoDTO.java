package com.use3w.grade.dto;

import java.util.List;

public record StudentEvaluationInfoDTO(
        AssessmentStudentInfoDTO student,
        List<AssessmentEvaluationQuestionDTO> questions,
        List<AssessmentAnswerDTO> answers,
        String rawFeedback,
        String finalFeedback,
        Double totalScore,
        Boolean evaluationCompleted
) {
}
