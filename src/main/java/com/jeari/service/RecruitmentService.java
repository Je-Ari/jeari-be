package com.jeari.service;

import com.jeari.dto.ApplicationRequest;
import com.jeari.dto.RecruitmentListResponse;
import com.jeari.dto.RecruitmentRequest;
import com.jeari.dto.RecruitmentResponse;
import com.jeari.entity.Club;
import com.jeari.entity.Recruitment;
import com.jeari.entity.RecruitmentStatus;
import com.jeari.repository.ClubRepository;
import com.jeari.repository.RecruitmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecruitmentService {

    final private ClubRepository clubRepository;
    final private RecruitmentRepository recruitmentRepository;

    // 모집 공고 생성
    public Integer createRecruitment(Integer clubId, RecruitmentRequest req) {
        // 부모 존재 확인
        if (!clubRepository.existsById(clubId)) {
            throw new EntityNotFoundException("동아리 없음: " + clubId);
        }

        Recruitment recruitment = Recruitment.builder()
                .clubId(clubId)
                .startDate(req.startDate())   // 또는 req.startDate()
                .endDate(req.endDate())
                .recruitTitle(req.recruitTitle())
                .recruitInfo(req.recruitInfo())
                .question(req.question())
                .build();

        return recruitmentRepository.save(recruitment).getId();
    }

    // 모집 공고 목록 반환
    public List<RecruitmentListResponse> getRecruitmentList(Integer clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동아리입니다."));

        List<Recruitment> recruitmentList = recruitmentRepository.findByClubId(clubId);

        return recruitmentList.stream()
                .map(recruitment -> new RecruitmentListResponse(
                        recruitment.getId(),
                        recruitment.getRecruitTitle(),
                        recruitment.getCreatedAt(),
                        recruitment.getStatus()
                ))
                .toList();
    }

    // 모집 공고 반환
    public RecruitmentResponse getRecruitment(Integer recruitmentId) {
        Recruitment recruitment = recruitmentRepository
                .findById(recruitmentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 삭제된 공고입니다."));

        return RecruitmentResponse.from(recruitment);
    }

//    // 유저 가입 신청 처리
//    public Integer applyToRecruitment(Integer recruitmentId, ApplicationRequest req) {
//
//
//    }
}
