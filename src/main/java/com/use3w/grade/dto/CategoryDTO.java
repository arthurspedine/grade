package com.use3w.grade.dto;

import jakarta.validation.constraints.NotNull;

public record CategoryDTO(
        @NotNull
        String name,
        @NotNull
        Double score
) {
}
