package com.sparta.orderapp.service.authservice;

import com.sparta.orderapp.TestUtil;
import com.sparta.orderapp.config.PasswordEncoder;
import com.sparta.orderapp.dto.user.KakaoUserDto;
import com.sparta.orderapp.entity.User;
import com.sparta.orderapp.exception.DuplicateEmailException;
import com.sparta.orderapp.repository.UserRepository;
import com.sparta.orderapp.service.AuthService;
import com.sparta.orderapp.service.KakaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class AuthServiceKakaoTest {
    @InjectMocks
    private AuthService authService;

    @InjectMocks
    private KakaoService kakaoService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void 카카오_회원가입시_이미_존재하는_이메일인경우_DEE를_반환한다() {
        KakaoUserDto kakaoUserDto = new KakaoUserDto(1L, "1234", "woo", "USER");
        User user = TestUtil.getUser(1L);

        given(userRepository.findByEmail(kakaoUserDto.getEmail())).willReturn(Optional.of(user));

        DuplicateEmailException exception = assertThrows(DuplicateEmailException.class,
                () -> kakaoService.signUpUseKakao(kakaoUserDto));

        assertEquals("이미 사용중인 이메일 입니다.", exception.getMessage());
    }

    @Test
    public void 카카오_회원가입에_성공한다() {
        KakaoUserDto kakaoUserDto = new KakaoUserDto(1L, "1234", "woo", "USER");
        User user = TestUtil.getUser(1L);

        given(userRepository.findByEmail(kakaoUserDto.getEmail())).willReturn(Optional.empty());

        kakaoService.signUpUseKakao(kakaoUserDto);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void 카카오_아이디_검색이_작동한다() {
        long kakaoId = 1L;

        given(userRepository.findBykakaoId(kakaoId)).willReturn(Optional.empty());

        User result = kakaoService.findByKakaoId(kakaoId);

        assertNull(result);
    }
}
