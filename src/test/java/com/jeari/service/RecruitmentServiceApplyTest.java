package com.jeari.service;

import com.jeari.config.CustomUserDetails;
import com.jeari.dto.ApplicationAnswer;
import com.jeari.dto.ApplicationSubmitRequest;
import com.jeari.entity.*;
import com.jeari.exception.*;
import com.jeari.repository.ClubRepository;
import com.jeari.repository.JoinRequestRepository;
import com.jeari.repository.RecruitmentRepository;
import com.jeari.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecruitmentServiceApplyTest {

    @Mock
    private ClubRepository clubRepository; // Though not used in these tests, it's part of the service's dependencies
    @Mock
    private RecruitmentRepository recruitmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JoinRequestRepository joinRequestRepository;

    @InjectMocks
    private RecruitmentService recruitmentService;

    private User testUser;
    private Recruitment testRecruitment;
    private CustomUserDetails customUserDetails;
    private ApplicationSubmitRequest applicationSubmitRequest;
    private List<ApplicationAnswer> answers;

    @BeforeEach
    void setUp() {
        // Mock User
        testUser = User.builder()
                .id(1)
                .name("Test User")
                .studentId("20201234")
                .email("test@example.com")
                .passwordHash("hashedpassword")
                .build();

        // Mock Recruitment
        testRecruitment = Recruitment.builder()
                .clubId(1)
                .recruitTitle("Test Recruitment")
                .recruitInfo("Info")
                .startDate(LocalDate.now().minusDays(1))
                .endDate(LocalDate.now().plusDays(1))
                .question(Collections.emptyList())
                .build();
        testRecruitment.setId(1);
        testRecruitment.setStatus(RecruitmentStatus.OPEN);

        // Mock CustomUserDetails
        customUserDetails = new CustomUserDetails(testUser);

        // Mock ApplicationSubmitRequest
        answers = List.of(new ApplicationAnswer(1, "Answer 1"));
        applicationSubmitRequest = new ApplicationSubmitRequest(answers);
    }

    @Test
    @DisplayName("모집 공고 지원 - 성공")
    void applyToRecruitment_success() {
        // Given
        when(userRepository.findByStudentId(testUser.getStudentId())).thenReturn(Optional.of(testUser));
        when(recruitmentRepository.findById(testRecruitment.getId())).thenReturn(Optional.of(testRecruitment));
        when(joinRequestRepository.findByRecruitmentAndUser(any(Recruitment.class), any(User.class))).thenReturn(Optional.empty());

        // When
        recruitmentService.applyToRecruitment(testRecruitment.getId(), applicationSubmitRequest, customUserDetails);

        // Then
        ArgumentCaptor<JoinRequest> joinRequestCaptor = ArgumentCaptor.forClass(JoinRequest.class);
        verify(joinRequestRepository, times(1)).save(joinRequestCaptor.capture());
        JoinRequest savedJoinRequest = joinRequestCaptor.getValue();

        assertThat(savedJoinRequest.getUser()).isEqualTo(testUser);
        assertThat(savedJoinRequest.getRecruitment()).isEqualTo(testRecruitment);
        assertThat(savedJoinRequest.getAnswers()).isEqualTo(answers);
        assertThat(savedJoinRequest.getStatus()).isEqualTo(JoinRequestStatus.PENDING);
    }

    @Test
    @DisplayName("모집 공고 지원 - 실패: 모집 공고를 찾을 수 없음")
    void applyToRecruitment_throwsRecruitmentNotFoundException() {
        // Given
        when(userRepository.findByStudentId(testUser.getStudentId())).thenReturn(Optional.of(testUser));
        when(recruitmentRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> recruitmentService.applyToRecruitment(999, applicationSubmitRequest, customUserDetails))
                .isInstanceOf(RecruitmentNotFoundException.class)
                .hasMessageContaining("모집 공고를 찾을 수 없습니다");

        verify(joinRequestRepository, never()).save(any(JoinRequest.class));
    }

    @Test
    @DisplayName("모집 공고 지원 - 실패: 모집 공고가 현재 모집 중이 아님")
    void applyToRecruitment_throwsRecruitmentNotOpenException() {
        // Given
        testRecruitment.setStatus(RecruitmentStatus.CLOSED);
        when(userRepository.findByStudentId(testUser.getStudentId())).thenReturn(Optional.of(testUser));
        when(recruitmentRepository.findById(testRecruitment.getId())).thenReturn(Optional.of(testRecruitment));

        // When & Then
        assertThatThrownBy(() -> recruitmentService.applyToRecruitment(testRecruitment.getId(), applicationSubmitRequest, customUserDetails))
                .isInstanceOf(RecruitmentNotOpenException.class)
                .hasMessageContaining("현재 모집 중인 공고가 아닙니다.");

        verify(joinRequestRepository, never()).save(any(JoinRequest.class));
    }

    @Test
    @DisplayName("모집 공고 지원 - 실패: 사용자를 찾을 수 없음")
    void applyToRecruitment_throwsUserNotFoundException() {
        // Given
        when(userRepository.findByStudentId(testUser.getStudentId())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> recruitmentService.applyToRecruitment(testRecruitment.getId(), applicationSubmitRequest, customUserDetails))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다");

        verify(joinRequestRepository, never()).save(any(JoinRequest.class));
    }

    @Test
    @DisplayName("모집 공고 지원 - 실패: 이미 지원함")
    void applyToRecruitment_throwsAlreadyAppliedException() {
        // Given
        when(userRepository.findByStudentId(testUser.getStudentId())).thenReturn(Optional.of(testUser));
        when(recruitmentRepository.findById(testRecruitment.getId())).thenReturn(Optional.of(testRecruitment));
        when(joinRequestRepository.findByRecruitmentAndUser(any(Recruitment.class), any(User.class))).thenReturn(Optional.of(new JoinRequest()));

        // When & Then
        assertThatThrownBy(() -> recruitmentService.applyToRecruitment(testRecruitment.getId(), applicationSubmitRequest, customUserDetails))
                .isInstanceOf(AlreadyAppliedException.class)
                .hasMessageContaining("이미 지원한 모집 공고입니다.");

        verify(joinRequestRepository, never()).save(any(JoinRequest.class));
    }
}
