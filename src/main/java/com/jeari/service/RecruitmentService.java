package com.jeari.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeari.config.CustomUserDetails;
import com.jeari.dto.ApplicationSubmitRequest;
import com.jeari.dto.RecruitmentCreateRequest;
import com.jeari.dto.RecruitmentListResponse;
import com.jeari.dto.RecruitmentResponse;
import com.jeari.entity.Club;
import com.jeari.entity.JoinRequest;
import com.jeari.entity.JoinRequestStatus;
import com.jeari.entity.Recruitment;
import com.jeari.entity.RecruitmentStatus;
import com.jeari.entity.User;
import com.jeari.exception.AlreadyAppliedException;
import com.jeari.exception.RecruitmentNotFoundException;
import com.jeari.exception.RecruitmentNotOpenException;
import com.jeari.exception.UserNotFoundException;
import com.jeari.repository.ClubRepository;
import com.jeari.repository.JoinRequestRepository;
import com.jeari.repository.RecruitmentRepository;
import com.jeari.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecruitmentService {

    final private ClubRepository clubRepository;
    final private RecruitmentRepository recruitmentRepository;
    final private UserRepository userRepository;
    final private JoinRequestRepository joinRequestRepository;
    // ObjectMapper is no longer needed for this service's logic.
    // It can be removed if not used elsewhere, but we'll leave it for now
    // in case other methods are added that might need it.
    // For this refactoring, we will simply not use it in `applyToRecruitment`.

    // 모집 공고 생성
    public Integer createRecruitment(Integer clubId, RecruitmentCreateRequest req) {
        // 부모 존재 확인
        if (!clubRepository.existsById(clubId)) {
            throw new EntityNotFoundException("동아리 없음: " + clubId);
        }

        Recruitment recruitment = Recruitment.builder()
                .clubId(clubId)
                .startDate(req.startDate())
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

    // 유저 가입 신청 처리
    public void applyToRecruitment(Integer recruitmentId, ApplicationSubmitRequest applicationRequest, CustomUserDetails userDetails) {
        User user = userRepository.findByStudentId(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + userDetails.getUsername()));

        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new RecruitmentNotFoundException("모집 공고를 찾을 수 없습니다: " + recruitmentId));

        // 모집 공고 상태 확인 (OPEN만 지원 가능)
        if (recruitment.getStatus() != RecruitmentStatus.OPEN) {
            throw new RecruitmentNotOpenException("현재 모집 중인 공고가 아닙니다. 상태: " + recruitment.getStatus());
        }

        // 이미 신청했는지 확인
        Optional<JoinRequest> existingJoinRequest = joinRequestRepository.findByRecruitmentAndUser(recruitment, user);
        if (existingJoinRequest.isPresent()) {
            throw new AlreadyAppliedException("이미 지원한 모집 공고입니다.");
        }

        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setRecruitment(recruitment);
        joinRequest.setUser(user);
        joinRequest.setAnswers(applicationRequest.userAnswers()); // Set the list directly
        joinRequest.setStatus(JoinRequestStatus.PENDING);
        joinRequest.setCreatedAt(OffsetDateTime.now());

        joinRequestRepository.save(joinRequest);
    }

}

