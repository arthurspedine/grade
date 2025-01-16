package com.use3w.grade.dto;

import java.util.List;
import java.util.UUID;

public record CreateAssessmentDTO(
        String name,
        List<AddClassToAssessmentDTO> classes,
        List<CreateAssessmentCategoryDTO> categories
) {

    public record AddClassToAssessmentDTO(
            UUID id
    ) {
    }
}
