package com.JeAri.repository;

import com.JeAri.model.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    // 클럽 ID로 최신 모집 공고 1개 조회 (startDate 기준 내림차순)
    Optional<Recruitment> findTopByClub_ClubIdOrderByStartDateDesc(Integer clubId);
}
