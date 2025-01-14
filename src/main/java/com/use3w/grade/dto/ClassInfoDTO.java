package com.use3w.grade.dto;

import java.util.List;

public record ClassInfoDTO(
        ClassDetailsDTO details,
        List<StudentDTO> students
) {
}
