package com.use3w.grade.dto;

import java.util.List;
import java.util.UUID;

public record AssessmentEditInfoDTO(
        String name,
        String assessmentDate,
        Boolean questionsEditable,
        List<EditableClassDTO> currentClasses,
        List<AvailableClassDTO> availableClasses,
        List<AssessmentQuestionDTO> questions
) {
    public record EditableClassDTO(
            UUID id,
            String name,
            Boolean removable
    ) {}

    public record AvailableClassDTO(
            UUID id,
            String name
    ) {}
}

