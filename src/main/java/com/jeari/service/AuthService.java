package com.jeari.service;

import com.jeari.dto.RegisterRequest;
import com.jeari.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;


    public String login(String studnet_id, String pw) {
        // 로그인 로직
        // 레포지토리로 pw 가져와서 비교

       return aceesstoken;
    }

    public void register(RegisterRequest req) {
        // 회원가입 로직
        // 이미 있는 회원인지 확인 후 save
        // return 뭐 줄지 고민
    }

    public String refreshToken(RegisterRequest req) {
        // 엑세스 토큰 따서 검증 로직 및 재발급


        return aceesstoken;
    }


}
