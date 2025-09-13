package com.jeari.service;

import com.jeari.dto.RecruitmentRequest;
import com.jeari.entity.Club;
import com.jeari.entity.Recruitment;
import com.jeari.entity.RecruitmentStatus;
import com.jeari.repository.ClubRepository;
import com.jeari.repository.RecruitmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RecruitmentService {

    final private ClubRepository clubRepository;
    final private RecruitmentRepository recruitmentRepository;

    public Integer createRecruitment(Integer clubId, RecruitmentRequest req) {
        // 행을 실제로 읽지 않고 FK만 설정하려면 존재 체크 → 프록시 참조 패턴 사용
        if (!clubRepository.existsById(clubId)) {
            throw new EntityNotFoundException("동아리 없음: " + clubId);
        }
        Club clubRef = clubRepository.getReferenceById(clubId); // SELECT 없이 프록시

        RecruitmentStatus recruitmentStatus = Optional.ofNullable(req.status())
                .orElse(RecruitmentStatus.OPEN);

        Recruitment recruitment = Recruitment.builder()
                .club(clubRef)
                .startDate(req.startDate())   // 또는 req.startDate()
                .endDate(req.endDate())
                .status(recruitmentStatus)         // 기본값이 있다면 여기서 지정 가능
                .recruitTitle(req.recruitTitle())
                .recruitInfo(req.recruitInfo())
                .question(req.question())
                .build();

        return recruitmentRepository.save(recruitment).getId();
    }
}
