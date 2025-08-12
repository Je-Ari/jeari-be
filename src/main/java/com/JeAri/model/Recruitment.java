package com.JeAri.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recruitmentId;

    @Column(nullable = false)
    private String recruitTitle;

    @Column(columnDefinition = "TEXT")
    private String recruitInfo;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecruitmentStatus status;

    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;
}
