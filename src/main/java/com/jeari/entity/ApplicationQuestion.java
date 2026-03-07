package com.jeari.entity;

public record ApplicationQuestion(
        Integer questionNum,
        String question,
        Boolean required
) {
}
