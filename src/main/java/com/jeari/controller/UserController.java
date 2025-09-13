package com.jeari.controller;

import com.jeari.entity.User;
import com.jeari.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    // 유저 정보
    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
