package com.use3w.grade.dto;

import com.use3w.grade.model.AssessmentAnswer;

import java.util.UUID;

public record CategoryAnswerDTO(
        UUID id,
        String name,
        Double score,
        Integer categoryNumber,
        Double answeredScore
) {
    public AssessmentAnswer getAs() { // DTO -> Entity
        return new AssessmentAnswer(id, answeredScore);
    }
}
