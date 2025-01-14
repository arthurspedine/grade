package com.use3w.grade.dto;

import com.use3w.grade.model.ECategory;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateClassDTO(
        @NotNull
        String name,
        @NotNull
        ECategory category,
        @NotNull
        MultipartFile file
) {
}
