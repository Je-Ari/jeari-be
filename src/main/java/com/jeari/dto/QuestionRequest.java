package com.jeari.dto;

public record QuestionRequest(
        String question,
        Boolean required
) {
}
