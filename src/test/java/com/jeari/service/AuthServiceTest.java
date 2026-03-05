package com.jeari.service;

import com.jeari.dto.LoginRequest;
import com.jeari.dto.SignUpRequest;
import com.jeari.entity.User;
import com.jeari.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional // 각 테스트가 끝난 후 롤백하여 DB 상태를 테스트 이전으로 되돌립니다.
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        // @Transactional으로 롤백되지만, 만약을 위해 DB를 직접 정리할 수도 있습니다.
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 - 성공")
    void signUp_success() {
        // given (주어진 상황)
        SignUpRequest request = new SignUpRequest("테스트유저", "20201234", "password123", "test@example.com");

        // when (무엇을 할 때)
        authService.signUp(request);

        // then (결과)
        User foundUser = userRepository.findByStudentId("20201234").orElseThrow();
        assertThat(foundUser.getName()).isEqualTo("테스트유저");
        assertThat(passwordEncoder.matches("password123", foundUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원가입 - 실패 (이미 존재하는 아이디)")
    void signUp_fail_when_username_exists() {
        // given
        // 먼저 사용자를 하나 생성
        User existingUser = User.builder()
                .name("기존유저")
                .studentId("20191111")
                .passwordHash(passwordEncoder.encode("password123"))
                .email("exist@example.com")
                .build();
        userRepository.save(existingUser);

        SignUpRequest request = new SignUpRequest("신규유저", "20191111", "newpassword", "new@example.com");

        // when & then
        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 가입된 학번입니다.");
    }


}
