package com.jeari.controller;

import com.jeari.dto.RecruitmentRequest;
import com.jeari.service.RecruitmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
@AllArgsConstructor
public class RecruitmentController {

    final private RecruitmentService recruitmentService;

    @PostMapping("/clubs/{clubid}/recruitments")
    public ResponseEntity<?> createRecruitment(@PathVariable Integer clubid, @Valid @RequestBody RecruitmentRequest req) {

        Integer recruitmentId = recruitmentService.createRecruitment(clubid, req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("recruitmentId", recruitmentId));
    }
}
