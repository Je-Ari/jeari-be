package com.jeari.config;

import com.jeari.entity.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration      //컴포넌트 스캔에 적용되도록 어노테이션을 달아줍니다.
@EnableWebSecurity  //모든 요청 URL이 스프링 시큐리티의 필터체인을 거치도록 하는 어노테이션입니다.
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 X
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 X
                // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터 연결 (있다면)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",       // 로그인 API (토큰 발급)
                                "/api/token/refresh",    // 토큰 재발급
                                "/api/auth/register",        // 회원가입
                                "/docs", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"    // swagger
                        ).permitAll()
                        .requestMatchers(
                                "/api/club/create"
                        ).hasRole(UserRole.USER.name())
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}

