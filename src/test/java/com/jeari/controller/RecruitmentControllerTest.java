package com.jeari.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeari.dto.RecruitmentRequest;
import com.jeari.service.RecruitmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class RecruitmentControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private RecruitmentService recruitmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("동아리 모집 공고 생성 - 성공")
    @WithMockUser(roles = {"USER", "PRESIDENT"})
    void createRecruitment_success() throws Exception {
        // given
        Integer clubId = 1;
        Integer recruitmentId = 1;
        RecruitmentRequest request = new RecruitmentRequest(
                LocalDate.of(2025, 9, 15),
                LocalDate.of(2025, 9, 30),
                com.jeari.entity.RecruitmentStatus.OPEN,
                "신입 부원 모집",
                "열정적인 신입 부원을 모집합니다.",
                "자기소개"
        );

        given(recruitmentService.createRecruitment(eq(clubId), any(RecruitmentRequest.class)))
                .willReturn(recruitmentId);

        // when & then
        mockMvc.perform(post("/clubs/{clubId}/recruitments", clubId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recruitmentId").value(recruitmentId));

        ArgumentCaptor<RecruitmentRequest> captor = ArgumentCaptor.forClass(RecruitmentRequest.class);
        verify(recruitmentService).createRecruitment(eq(clubId), captor.capture());
        RecruitmentRequest capturedRequest = captor.getValue();
        assertThat(capturedRequest.recruitTitle()).isEqualTo(request.recruitTitle());
    }
}