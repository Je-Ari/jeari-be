package com.jeari.dto;

import com.jeari.entity.Recruitment;
import com.jeari.entity.RecruitmentStatus;

import java.time.OffsetDateTime;

public record RecruitmentListResponse(
        Integer id,
        String title,
        OffsetDateTime createdAt,
        RecruitmentStatus status
) {
    public static RecruitmentListResponse from(Recruitment recruitment) {
        return new RecruitmentListResponse(
                recruitment.getId(),
                recruitment.getRecruitTitle(),
                recruitment.getCreatedAt(),
                recruitment.getStatus()
        );
    }
}
