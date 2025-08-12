package com.JeAri.service;

import com.JeAri.dto.ClubSummaryResponse;
import com.JeAri.model.Recruitment;
import com.JeAri.model.RecruitmentStatus;
import com.JeAri.repository.ClubRepository;
import com.JeAri.model.Club;
import com.JeAri.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final RecruitmentRepository recruitmentRepository;

    public List<ClubSummaryResponse> getAllClubs() {
        List<Club> clubs = clubRepository.findAll();    // 동아리 전부 가져오기
        clubs.forEach(club -> System.out.println(club.getName()));
        return clubs.stream()
                .map(club -> {
                    // 모집 상태가 '모집중'인 최신 recruitment이 있는지 확인
                    Optional<Recruitment> latest = recruitmentRepository
                            .findTopByClub_ClubIdOrderByStartDateDesc(club.getClubId());

                    RecruitmentStatus status = latest
                            .map(Recruitment::getStatus)
                            .orElse(RecruitmentStatus.CLOSED);

                    return new ClubSummaryResponse(club, status);
                })
                .toList();
    }
}
