package com.jeari.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.access-token-validity-seconds}") private long accessValidity;
    @Value("${jwt.refresh-token-validity-seconds}") private long refreshValidity;

    private Key key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserDetails user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getUsername())         // 실제로는 학번 담김
                .claim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(accessValidity)))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(refreshValidity)))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
