package com.jeari.entity;

public record RecruitmentQuestion(
        Integer questionNum,
        String question,
        Boolean required
) {
}
