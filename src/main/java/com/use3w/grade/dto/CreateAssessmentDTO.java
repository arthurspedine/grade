package com.use3w.grade.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateAssessmentDTO(
        @NotNull
        String name,
        @NotNull
        List<AddClassToAssessmentDTO> classes,
        @NotNull
        List<CreateAssessmentQuestionDTO> questions
) {

    public record AddClassToAssessmentDTO(
            UUID id
    ) {
    }
}
