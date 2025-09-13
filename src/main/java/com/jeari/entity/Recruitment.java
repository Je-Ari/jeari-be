package com.jeari.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "recruitment")
public class Recruitment {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_id", nullable = false)
    private Integer id;

    // Hibernate가 insert 시점에 자동 세팅
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz")
    private OffsetDateTime createdAt;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecruitmentStatus status;

    @Size(max = 255)
    @NotNull
    @Column(name = "recruit_title", nullable = false)
    private String recruitTitle;

    @Column(name = "recruit_info", length = Integer.MAX_VALUE)
    private String recruitInfo;

    @Column(name = "question", length = Integer.MAX_VALUE)
    private String question;

    @Builder
    public Recruitment(
            Club club,
            LocalDate startDate,
            LocalDate endDate,
            RecruitmentStatus status,
            String recruitTitle,
            String recruitInfo,
            String question
    ) {
        this.club = club;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.recruitTitle = recruitTitle;
        this.recruitInfo = recruitInfo;
        this.question = question;
    }


    // 상태 변경
    public void changeStatus(RecruitmentStatus newStatus) {
        this.status = newStatus;
    }

    // 날짜 무결성 간단 체크
    @AssertTrue(message = "endDate는 startDate보다 빠를 수 없습니다.")
    private boolean isValidDateRange() {
        return endDate == null || !endDate.isBefore(startDate);
    }
}