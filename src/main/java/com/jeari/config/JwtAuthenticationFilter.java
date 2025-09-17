package com.jeari.config;

import com.jeari.service.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    private static final AntPathMatcher matcher = new AntPathMatcher();
    private static final String[] WHITELIST = {
            "/auth/**",
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/docs"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        for (String p : WHITELIST) {
            if (matcher.match(p, path)) return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull FilterChain chain)
            throws ServletException, IOException {

        String token = resolveToken(req);
        if (token != null && tokenProvider.validate(token)) {
            String studentId = tokenProvider.getUsername(token);                    // 아이디로 학번 사용
            UserDetails user = userDetailsService.loadUserByUsername(studentId);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()); // JWT로 인증 했으므로, 비밀번호 필요x
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));            // IP, 세션 ID 등 부가 정보 추가

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(req, res);       // 다음 필터로 데이터 넘김
    }

    private String resolveToken(HttpServletRequest req) {
        // 1) Authorization 헤더
        String bearer = req.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        // 2) 쿠키
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if ("accessToken".equals(c.getName())) return c.getValue();
            }
        }
        return null;
    }
}
