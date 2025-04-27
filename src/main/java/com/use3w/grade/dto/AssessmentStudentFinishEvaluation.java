package com.use3w.grade.dto;

import java.util.List;

public record AssessmentStudentFinishEvaluation(
        AssessmentStudentInfoDTO student,
        List<AssessmentAnswerDTO> questions,
        String rawFeedback,
        String finalFeedback
) {
}
