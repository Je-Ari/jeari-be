package com.jeari.dto;

import java.util.List;

public record ApplicationSubmitRequest(
    List<ApplicationAnswer> userAnswers
) {
}
