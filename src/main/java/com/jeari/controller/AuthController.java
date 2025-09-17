package com.jeari.controller;

import com.jeari.dto.LoginRequest;
import com.jeari.dto.SignUpRequest;
import com.jeari.service.AuthService;
import com.jeari.service.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private  final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "학번, 비밀번호")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.studentId(), req.password())
        );
        UserDetails principal = (UserDetails) auth.getPrincipal();
        String access = jwtTokenProvider.generateAccessToken(principal);
        String refresh = jwtTokenProvider.generateRefreshToken(principal.getUsername());

        // ✅ 쿠키에 accessToken 저장 (HttpOnly)
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", access)
                .httpOnly(true)        // JS에서 접근 불가
                .secure(true)          // HTTPS에서만
                .path("/")             // 전체 경로에서 사용
                .maxAge(3600)          // 1시간
                .sameSite("Strict")    // CSRF 방어
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refresh)
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh") // refresh는 특정 경로에서만 사용
                .maxAge(1209600)       // 14일
                .sameSite("Strict")
                .build();


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(Map.of("message", "Login success"));
    }

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest req) {
        authService.signUp(req);

        return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
    }


}
