package com.use3w.grade.dto;

import java.util.List;

public record AssessmentEvaluationQuestionDTO(
        Integer questionNumber,
        List<EvaluationCategoryDTO> categories
) {
}
