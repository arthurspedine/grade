package com.use3w.grade.dto;

import java.util.UUID;

public record AssessmentStudentInfoDTO(
        UUID id,
        UUID assessmentId,
        UUID classId,
        String name,
        String rm
) {
}
