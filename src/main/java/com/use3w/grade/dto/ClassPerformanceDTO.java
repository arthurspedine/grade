package com.use3w.grade.dto;

import java.util.UUID;

public record ClassPerformanceDTO(
        UUID id,
        String name,
        Double performanceRate
) {
}
