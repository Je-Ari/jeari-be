package com.jeari.service;

import com.jeari.dto.ClubSummaryResponse;
import com.jeari.entity.Recruitment;
import com.jeari.entity.RecruitmentStatus;
import com.jeari.repository.ClubRepository;
import com.jeari.entity.Club;
import com.jeari.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final RecruitmentRepository recruitmentRepository;

    // 동아리 정보 + 모집 여부 반환
    public List<ClubSummaryResponse> getAllClubs() {
        List<Club> clubs = clubRepository.findAll();    // 동아리 전부 가져오기
        clubs.forEach(club -> System.out.println(club.getName())); // 디버깅용 출력
        return clubs.stream()
                .map(club -> {
                    // 최신 recruitment이 있는지 확인
                    Optional<Recruitment> latest = recruitmentRepository
                            .findTopByClub_IdOrderByStartDateDesc(club.getId());

                    RecruitmentStatus status = latest
                            .map(Recruitment::getStatus)
                            .orElse(RecruitmentStatus.CLOSED);

                    return new ClubSummaryResponse(club, status);
                })
                .toList();
    }

    // 모집 중인 동아리
    public List<ClubSummaryResponse> getOpenClubs() {
        return getAllClubs().stream()
                .filter(dto -> dto.getRecruitmentStatus() == RecruitmentStatus.OPEN)
                .toList();
    }

    // 동아리 추가

    // 동아리 삭제

    // 동아리 회원 조회
}
