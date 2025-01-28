package com.use3w.grade.dto;

import jakarta.validation.constraints.NotNull;

public record CreateCategoryDTO(
        @NotNull
        String name,
        @NotNull
        Integer score
) {
}
