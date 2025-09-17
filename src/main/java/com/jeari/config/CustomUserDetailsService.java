package com.jeari.config;

import com.jeari.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음: " + studentId));
    }
}