package com.sparta.orderapp.controller;


import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.dto.sign.SignupRequestDto;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.dto.user.LoginRequestDto;
import com.sparta.orderapp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/users/signup")
    public ResponseEntity<Object> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        authService.signUp(signupRequestDto);
        return ResponseEntity.ok().build();
    }

    /***
     * 로그인
     * @param requestDto : 로그인할 이메일과 비밀번호
     * @return JWT토큰 헤더로 반환
     */
    @PostMapping("/users")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).header("Authorization",authService.login(requestDto)).build();
    }

    /**
     * @Auth 사용 방법이 담긴 test 메서드
     * @param user 현재 로그인된 유저 정보가 담긴 user 객체
     * @return 200 : OK
     */
    @GetMapping("/users/test")
    public ResponseEntity<Object> test(@Auth AuthUser user) {
        return ResponseEntity.ok().body(user);
    }
}

