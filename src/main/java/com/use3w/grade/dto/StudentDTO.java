package com.use3w.grade.dto;

import jakarta.validation.constraints.NotNull;

public record StudentDTO(
        @NotNull
        String rm,
        @NotNull
        String name
) {
}
