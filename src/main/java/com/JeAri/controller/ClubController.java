package com.JeAri.controller;

import com.JeAri.dto.ClubSummaryResponse;
import com.JeAri.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @GetMapping
    public List<ClubSummaryResponse> getAllClubs() {
        return clubService.getAllClubs();
    }
}
