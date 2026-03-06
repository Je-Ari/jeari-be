package com.jeari.dto;

import com.jeari.entity.Recruitment;
import com.jeari.entity.RecruitmentStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;

public record RecruitmentResponse(
        Integer id,
        Integer club_id,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        LocalDate startDate,
        LocalDate endDate,
        RecruitmentStatus status,
        String recruitTitle,
        String recruitInfo,
        String question
        ) {
    public static RecruitmentResponse from(Recruitment recruitment) {
        return new RecruitmentResponse(
                recruitment.getId(),
                recruitment.getClubId(),
                recruitment.getCreatedAt(),
                recruitment.getUpdatedAt(),
                recruitment.getStartDate(),
                recruitment.getEndDate(),
                recruitment.getStatus(),
                recruitment.getRecruitTitle(),
                recruitment.getRecruitInfo(),
                recruitment.getQuestion()
        );
    }
}
