package com.jeari.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record RecruitmentCreateRequest(
        @NotNull
        @Schema(example = "2025-09-15", description = "모집 시작일 (YYYY-MM-DD)")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @Schema(example = "2025-10-01", description = "모집 종료일 (선택)")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate,

        @NotBlank @Size(max = 255)
        @Schema(example = "24기 운영진 모집")
        String recruitTitle,

        @Schema(description = "모집 상세 설명(선택)")
        String recruitInfo,

        @Schema(description = "지원 질문(선택)")
        List<QuestionRequest> question
) {
    @AssertTrue(message = "endDate는 startDate보다 빠를 수 없습니다.")
    @Schema(hidden = true)
    public boolean isValidDateRange() {
        return endDate == null || !endDate.isBefore(startDate);
    }
}
