package com.use3w.grade.dto;

import java.util.List;

public record AssessmentAnswerDTO(
        Integer questionNumber,
        List<CategoryAnswerDTO> categories
) {
}
