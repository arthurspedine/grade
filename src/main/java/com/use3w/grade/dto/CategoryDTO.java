package com.use3w.grade.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CategoryDTO(
        @NotNull
        String name,
        @NotNull
        Double score,
        @NotNull
        @Positive
        Integer categoryNumber
) {
}
