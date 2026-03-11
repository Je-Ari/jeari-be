package com.jeari.entity;

import com.jeari.dto.QuestionRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "recruitment")
public class Recruitment {

    @NotNull
    @Column(name = "club_id", nullable = false)
    private Integer clubId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_id", nullable = false)
    private Integer id;

        // Hibernate가 insert 시점에 자동 세팅
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp        // 생성될때 createsAt과 동일한 값을 넣고, 업데이트시 변경됨
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamptz")
    private OffsetDateTime updatedAt;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name="recruitment_status", nullable = false)
    private RecruitmentStatus status;

    @Size(max = 255)
    @NotNull
    @Column(name = "recruit_title", nullable = false)
    private String recruitTitle;

    @Column(name="recruit_info", columnDefinition = "TEXT")
    private String recruitInfo;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<RecruitmentQuestion> question;

    @Builder
    public Recruitment(
            Integer clubId,
            LocalDate startDate,
            LocalDate endDate,
            String recruitTitle,
            String recruitInfo,
            List<QuestionRequest> question
    ) {
        this.clubId = clubId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = calculateStatus();
        this.recruitTitle = recruitTitle;
        this.recruitInfo = recruitInfo;
        this.question = setQuestionNum(question);
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

    // 질문에 번호를 메기는 메서드
    public List<RecruitmentQuestion> setQuestionNum(
            List<QuestionRequest> requests
    ) {
        return IntStream.range(0, requests.size())
                .mapToObj(i -> {
                    QuestionRequest request = requests.get(i);

                    return new RecruitmentQuestion(
                            i+1,
                            request.question(),
                            request.required()
                    );
                })
                .toList();
    }

    public RecruitmentStatus calculateStatus() {
        LocalDate now = LocalDate.now();

        if(now.isBefore(startDate)) {
            return RecruitmentStatus.UPCOMING;
        }
        if(now.isAfter(endDate)) {
            return RecruitmentStatus.CLOSED;
        }

        return RecruitmentStatus.OPEN;
    }
}