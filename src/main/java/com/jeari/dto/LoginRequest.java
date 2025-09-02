package com.jeari.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "학번은 필수 입력 값입니다.")
        @Pattern(regexp = "\\d{10}", message = "학번은 숫자 10자리여야 합니다.")
        String studentId,

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Size(min = 8, max = 64, message = "비밀번호는 8자 이상 64자 이하로 입력해야 합니다.")
        String password
) {
}
