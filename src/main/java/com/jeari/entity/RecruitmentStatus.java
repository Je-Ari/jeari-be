package com.jeari.entity;

import lombok.Getter;

@Getter
public enum RecruitmentStatus {
    OPEN("모집중"),
    CLOSED("마감"),
    UPCOMING("모집 예정"),
    ALWAYS("상시 모집");

    private final String label;

    RecruitmentStatus(String label) {
        this.label = label;
    }

}