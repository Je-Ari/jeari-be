package com.jeari.controller;

import com.jeari.entity.User;
import com.jeari.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    // 회원 가입
    @PostMapping
    public User register(@RequestBody User user) {
        return userRepository.save(user);           // 컨트롤러에서 레포지토리 직접 조작 지양
    }
    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // 로그인

    // 유저 정보
}
