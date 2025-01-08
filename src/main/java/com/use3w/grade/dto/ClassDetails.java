package com.use3w.grade.dto;

import com.use3w.grade.model.ECategory;

import java.util.UUID;

public record ClassDetails(
        UUID id,
        String name,
        ECategory category,
        Boolean active
) {
}
