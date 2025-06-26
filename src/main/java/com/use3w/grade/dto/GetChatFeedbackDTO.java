package com.use3w.grade.dto;

import jakarta.validation.constraints.NotBlank;

public record GetChatFeedbackDTO(
        @NotBlank
        String answeredCategories,
        @NotBlank
        String rawFeedback
) {
}
