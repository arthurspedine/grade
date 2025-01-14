package com.use3w.grade.dto;

import com.use3w.grade.model.ECategory;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EditClassDTO(
        @NotNull
        UUID id,
        String name,
        ECategory category
) {
}
