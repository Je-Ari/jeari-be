package com.jeari.dto;

import com.jeari.entity.Club;
import com.jeari.entity.ClubCategory;
import com.jeari.entity.RecruitmentStatus;
import lombok.Getter;

@Getter
public class ClubSummaryResponse {
    private final Integer id;
    private final String name;
    private final String description;
    private final ClubCategory category;
    private final String subcategory;
    private final RecruitmentStatus recruitmentStatus;

    public ClubSummaryResponse(Club club, RecruitmentStatus status) {
        this.id = club.getId();
        this.name = club.getName();
        this.description = club.getDescription();
        this.category = club.getCategory();
        this.subcategory = club.getSubcategory();
        this.recruitmentStatus = status;
    }
}
