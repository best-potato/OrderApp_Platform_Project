package com.sparta.orderapp.service.authservice;

import com.sparta.orderapp.TestUtil;
import com.sparta.orderapp.config.JwtUtil;
import com.sparta.orderapp.config.PasswordEncoder;
import com.sparta.orderapp.dto.user.LoginRequestDto;
import com.sparta.orderapp.entity.User;
import com.sparta.orderapp.exception.NoSignedUserException;
import com.sparta.orderapp.exception.WrongPasswordException;
import com.sparta.orderapp.repository.UserRepository;
import com.sparta.orderapp.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class AuthServiceLoginTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    public void 로그인시_존재하지않는_이메일을_입력할경우_IRE를_반환한다() {
        LoginRequestDto signinRequest = new LoginRequestDto("a@b.com", "1234");

        given(userRepository.findByEmail(signinRequest.getEmail())).willReturn(Optional.empty());

        NoSignedUserException exception = assertThrows(NoSignedUserException.class,
                () -> authService.login(signinRequest));

        assertEquals("가입되지 않은 유저입니다.", exception.getMessage());
    }

    @Test
    public void 로그인시_이미_탈퇴한_회원이라면_NSUE를_반환한다() {
        LoginRequestDto signinRequest = new LoginRequestDto("a@b.com", "1234");
        User user = TestUtil.getDisableUser();

        given(userRepository.findByEmail(signinRequest.getEmail())).willReturn(Optional.of(user));

        NoSignedUserException exception = assertThrows(NoSignedUserException.class,
                () -> authService.login(signinRequest)
        );

        assertEquals("가입되지 않은 유저입니다.", exception.getMessage());
    }

    @Test
    public void 로그인시_비밀번호가_일치하지_않으면_WPE을_반환한다() {
        long userId = 1L;
        LoginRequestDto signinRequest = new LoginRequestDto("a@b.com", "1234");
        User user = TestUtil.getUser(userId);

        given(userRepository.findByEmail(signinRequest.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())).willReturn(false);

        WrongPasswordException exception = assertThrows(WrongPasswordException.class,
                () -> authService.login(signinRequest)
        );

        assertEquals("잘못된 비밀번호입니다.", exception.getMessage());
    }

    @Test
    public void 로그인에_성공한다() {
        long userId = 1L;
        LoginRequestDto signinRequest = new LoginRequestDto("a@b.com", "1234");
        User user = TestUtil.getUser(userId);

        given(userRepository.findByEmail(signinRequest.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())).willReturn(true);
        given(jwtUtil.createToken(user)).willReturn("token");

        String result = authService.login(signinRequest);

        assertNotNull(result);
    }
}
