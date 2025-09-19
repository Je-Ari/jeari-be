package com.jeari.config;

import com.jeari.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements org.springframework.security.core.userdetails.UserDetails {
    private final Integer id;               // 필요하면 보관
    private final String studentId;         // username으로 쓸 값
    private final String password;          // 해시
    private final String name;              // 화면/응답에 쓸 이름
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.studentId = user.getStudentId();
        this.password = user.getPasswordHash();
        this.name = user.getName();
        this.email = user.getEmail();
        this.authorities = List.of(new SimpleGrantedAuthority(user.getUserRole().name()));
        // 만약 스프링의 hasRole("USER") 스타일을 쓰면: new SimpleGrantedAuthority("ROLE_" + user.getUserRole().getSimple())
    }

    @Override public String getUsername() { return studentId; }
    @Override public String getPassword() { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}