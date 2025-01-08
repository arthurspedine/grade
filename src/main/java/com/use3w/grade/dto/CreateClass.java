package com.use3w.grade.dto;

import com.use3w.grade.model.ECategory;
import jakarta.validation.constraints.NotNull;

public record CreateClass(
        @NotNull
        String name,
        @NotNull
        ECategory category
) {
}
