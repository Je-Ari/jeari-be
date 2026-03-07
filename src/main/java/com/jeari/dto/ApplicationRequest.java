package com.jeari.dto;

import com.jeari.entity.ApplicationAnswer;

import java.util.List;

public record ApplicationRequest(
    List<ApplicationAnswer> userAnswers
) {
}
