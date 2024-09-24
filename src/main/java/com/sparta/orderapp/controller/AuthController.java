package com.sparta.orderapp.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.config.JwtUtil;
import com.sparta.orderapp.dto.sign.DeleteAccountRequestDto;
import com.sparta.orderapp.dto.sign.SignupRequestDto;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.dto.user.LoginRequestDto;
import com.sparta.orderapp.service.AuthService;
import com.sparta.orderapp.service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class AuthController {

    private final AuthService authService;
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    /**
     * 유저가 회원가입을 처리할 메서드
     * @param signupRequestDto
     * @return 200 : Ok
     */
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
     * 회원탈퇴 관련 URL을 처리하는 메서드
     * @param user 현재 로그인 중인 유저 정보
     * @param httpServletRequest Token값이 담긴 HttpServletRequest 객체
     * @param requestDto 유저가 기입한 암호 정보
     * @return 200 : 삭제 성공
     */
    @DeleteMapping("/users")
    public ResponseEntity<Object> deleteAccount(@Auth AuthUser user, HttpServletRequest httpServletRequest, @RequestBody DeleteAccountRequestDto requestDto) {
        String token = httpServletRequest.getHeader("Authorization");
        String jwt = jwtUtil.substringToken(token);
        authService.deleteAccount(jwt, user, requestDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 카카오 유저로 로그인하는 메서드
     * @param code 로그인 URI 코드
     * @return 로그인 후 JwtToken 반환
     */
    @GetMapping("/users/kakao")
    public ResponseEntity<Object> kakaoUserLogin(@RequestParam String code) throws JsonProcessingException {
        String token = kakaoService.kakaoUserLogin(code);
        return ResponseEntity.status(HttpStatus.OK).header("Authorization",token).build();
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

