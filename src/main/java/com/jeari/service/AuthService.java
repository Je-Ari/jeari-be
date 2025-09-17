package com.jeari.service;

import com.jeari.dto.SignUpRequest;
import com.jeari.entity.User;
import com.jeari.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest req) {
        if(userRepository.findByStudentId(req.studentId()).isPresent()){
            throw new IllegalArgumentException("이미 가입된 학번입니다.");
        }

        User user = User.builder()
                .name(req.name())
                .studentId(req.studentId())
                .passwordHash(passwordEncoder.encode(req.password()))
                .email(req.email())
                .build();

        userRepository.save(user);
    }


}
