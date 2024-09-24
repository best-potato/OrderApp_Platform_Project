package com.sparta.orderapp.service.authservice;

import com.sparta.orderapp.TestUtil;
import com.sparta.orderapp.config.JwtUtil;
import com.sparta.orderapp.config.PasswordEncoder;
import com.sparta.orderapp.dto.sign.SignupRequestDto;
import com.sparta.orderapp.entity.User;
import com.sparta.orderapp.exception.DuplicateEmailException;
import com.sparta.orderapp.repository.UserRepository;
import com.sparta.orderapp.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class AuthServiceSignupTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    public void 회원가입시_이미_존재하는_이메일인경우_DEE를_반환한다() {
        SignupRequestDto signupRequest = new SignupRequestDto("a@b.com", "1234", "woo", "USER");
        User user = TestUtil.getUser(1L);

        given(userRepository.findByEmail(signupRequest.getEmail())).willReturn(Optional.of(user));

        DuplicateEmailException exception = assertThrows(DuplicateEmailException.class,
                () -> authService.signUp(signupRequest));

        assertEquals("이미 사용중인 이메일 입니다.", exception.getMessage());
    }

    @Test
    public void 회원가입에_성공한다() {
        SignupRequestDto signupRequest = new SignupRequestDto("a@b.com", "1234", "woo", "USER");
        User user = TestUtil.getUser(1L);

        given(userRepository.findByEmail(signupRequest.getEmail())).willReturn(Optional.empty());

        authService.signUp(signupRequest);

        verify(userRepository, times(1)).save(any());
    }
}
