package com.jeari.controller;

import com.jeari.dto.ApplicationRequest;
import com.jeari.dto.RecruitmentListResponse;
import com.jeari.dto.RecruitmentRequest;
import com.jeari.service.RecruitmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class RecruitmentController {

    final private RecruitmentService recruitmentService;

    @PostMapping("/clubs/{clubId}/recruitments")
    @Operation(summary = "모집 공고 작성", description = "모집 공고 생성")
    public ResponseEntity<?> createRecruitment(@PathVariable Integer clubId, @Valid @RequestBody RecruitmentRequest req) {

        Integer recruitmentId = recruitmentService.createRecruitment(clubId, req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("recruitmentId", recruitmentId));
    }


    @GetMapping("/clubs/{clubId}/recruitments")
    @Operation(summary = "모집 공고 목록 조회", description = "특정 동아리 모집 공고 목록 조회(공고 자체 x, 공고 리스트<- 제목, 작성일, 상태 등)")
    public ResponseEntity<List<RecruitmentListResponse>> getRecruitments(@PathVariable Integer clubId) {

        return ResponseEntity.ok(recruitmentService.getRecruitmentList(clubId));
    }

    @GetMapping("/recruitments/{recruitmentId}")
    @Operation(summary = "모집 공고 조회", description = "모집 공고 조회(공고 자체)")
    public ResponseEntity<?> getRecruitment(@PathVariable Integer recruitmentId) {

        return ResponseEntity.ok(recruitmentService.getRecruitment(recruitmentId));
    }

//    @PostMapping("/recruitments/{id}/applications")
//    public ResponseEntity<?> createApplication(@PathVariable Integer id, @RequestBody ApplicationRequest req) {
//
//        return ResponseEntity.ok(recruitmentService.applyToRecruitment(id, req));
//    }

}
