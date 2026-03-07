package com.jeari.entity;

public record RecruitmentQuestionRequest(
        String question,
        Boolean required
) {
}
