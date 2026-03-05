package com.jeari.config;

import com.jeari.entity.ClubRole;
import com.jeari.entity.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                .authorizeHttpRequests(auth -> auth     // 인가
                        .requestMatchers(
                                "/auth/**",       // 로그인 API (토큰 발급)
                                "/docs", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",    // swagger
                                "/clubs/{clubId}/recruitments" // 모집 공고 조회
                        ).permitAll()
                        .requestMatchers(
                                "/club/create"
                        ).hasAuthority(UserRole.ROLE_USER.name())
                        .requestMatchers(
                                "/clubs/{clubid}/recruitments"
                        ).hasRole(ClubRole.PRESIDENT.name())
                        .anyRequest().authenticated()
                );

        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

