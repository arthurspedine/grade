package com.use3w.grade.dto;

import com.use3w.grade.model.ECategory;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateClassDTO(
        @NotNull
        String name,
        @NotNull
        ECategory category,
        @NotNull
        List<StudentDTO> students
) {
}
