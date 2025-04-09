package com.use3w.grade.dto;

import java.util.UUID;

public record EvaluationCategoryDTO(
        UUID id,
        String name,
        Double score
) {
}
