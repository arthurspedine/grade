package com.use3w.grade.dto;

import java.util.List;

public record AssessmentInfoDTO(
        String name,
        List<StudentDTO> students
) {
}
