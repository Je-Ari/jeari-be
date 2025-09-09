package com.jeari.dto;

import com.jeari.entity.ClubCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateClubRequest(
        @NotBlank(message = "동아리명은 필수 입력 값입니다.")
        @Size(min = 1, max = 50, message = "동아리명은 한 글자 이상 50자 이하로 입력해 주세요.")
        String name,

        String description,

        @Schema(description = "동아리 카테고리", example = "ARTS")
        @NotBlank(message = "카테고리를 지정해 주세요.")
        ClubCategory category,

        @Size(min = 1, max = 15, message = "서브 카테고리는 한 글자 이상 15자 이하로 입력해 주세요.")
        String subcategory
) {
}
