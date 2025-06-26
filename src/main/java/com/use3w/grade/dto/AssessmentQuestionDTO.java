package com.use3w.grade.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record AssessmentQuestionDTO(
        @NotNull
        @Positive
        Integer questionNumber,
        @NotNull
        List<CategoryDTO> categories
) {
}
