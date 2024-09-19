package com.sparta.orderapp.controller;


import com.sparta.orderapp.dto.user.LoginRequestDto;
import com.sparta.orderapp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /***
     * 로그인
     * @param requestDto : 로그인할 이메일과 비밀번호
     * @return JWT토큰 헤더로 반환
     */

    @PostMapping("/auth/login")
    public ResponseEntity<Object> loginResponseDto(@Valid @RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).header("Authorization",authService.login(requestDto)).build();
    }

}

