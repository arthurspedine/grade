package com.use3w.grade.dto;

import java.util.UUID;

public record AssessmentStudentInfoDTO(
        UUID id,
        String name,
        String rm
) {
}
