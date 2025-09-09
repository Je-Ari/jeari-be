package com.jeari.controller;

import com.jeari.dto.LoginRequest;
import com.jeari.dto.RegisterRequest;
import com.jeari.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "학번, 비밀번호")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        authService.login(req.studentId(), req.password());
        // 쿠키 리턴 로직

        return ResponseEntity.ok(Map.of("message", "로그인 성공"));
    }

    @PostMapping("/regist")
    @Operation(summary = "회원가입")
    public ResponseEntity<?> regist(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);

        return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
    }


}
