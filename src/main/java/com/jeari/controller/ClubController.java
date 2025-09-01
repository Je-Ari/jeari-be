package com.jeari.controller;

import com.jeari.dto.ClubSummaryResponse;
import com.jeari.entity.Club;
import com.jeari.repository.ClubRepository;
import com.jeari.service.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;
    private final ClubRepository clubRepository;

    @Operation(summary = "동아리 목록 조회", description = "전체 동아리 조회 + 모집 상태")
    @GetMapping
    public List<ClubSummaryResponse> getAllClubs() {        // 반환 dto로 데이터 감싸기 필요
        return clubService.getAllClubs();
    }

    @GetMapping("/open")
    @Operation(summary = "모집중 동아리 목록", description = "현재 모집중(OPEN)인 동아리만 조회")
    public List<ClubSummaryResponse> getOpenClubs() {
        return clubService.getOpenClubs();
    }

    // @GetMapping("/{id}")
    // @Operation(summary = "동아리 상세 정보", description = "동아리 상세 페이지에 들어갈 정보 조회")

    // 동아리 회원 목록 조회

    @Operation(summary = "동아리 추가", description = "동아리 신규 추가 - admin 계정만 가능")
    @PostMapping
    public Club addClub(@RequestBody Club club){    // 필요시 req DTO로 입력 파라메터 바꾸기
        return clubRepository.save(club);           // 컨트롤러에서 레포 직접 접근 지양
    }

    // 동아리 삭제
}
