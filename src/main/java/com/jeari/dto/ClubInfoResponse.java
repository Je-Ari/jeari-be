package com.jeari.dto;

import com.jeari.entity.ClubCategory;


public record ClubInfoResponse(
        String name,
        String description,
        ClubCategory category,
        String subcategory
) {
}
