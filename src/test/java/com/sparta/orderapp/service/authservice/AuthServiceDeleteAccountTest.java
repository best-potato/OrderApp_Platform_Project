package com.sparta.orderapp.service.authservice;

import com.sparta.orderapp.TestUtil;
import com.sparta.orderapp.config.PasswordEncoder;
import com.sparta.orderapp.dto.sign.DeleteAccountRequestDto;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.entity.User;
import com.sparta.orderapp.exception.NoSignedUserException;
import com.sparta.orderapp.exception.WrongPasswordException;
import com.sparta.orderapp.repository.UserRepository;
import com.sparta.orderapp.service.AuthService;
import com.sparta.orderapp.service.BlacklistTokenService;
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
public class AuthServiceDeleteAccountTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BlacklistTokenService blacklistTokenService;

    @Test
    public void 회원탈퇴시_유저가_존재하지_않으면_NUE을_반환한다() {
        long userId = 1L;
        String token = "token";
        DeleteAccountRequestDto deleteAccountRequestDto = new DeleteAccountRequestDto("1234");
        User user = TestUtil.getUser(userId);
        AuthUser authUser = TestUtil.authUser(userId);

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        NoSignedUserException exception = assertThrows(NoSignedUserException.class,
                () -> authService.deleteAccount(token, authUser, deleteAccountRequestDto)
        );

        assertEquals("가입되지 않은 유저입니다.", exception.getMessage());
    }

    @Test
    public void 회원탈퇴시_비밀번호가_일치하지_않으면_NPE을_반환한다() {
        long userId = 1L;
        String token = "token";
        DeleteAccountRequestDto deleteAccountRequestDto = new DeleteAccountRequestDto("1234");
        User user = TestUtil.getUser(userId);
        AuthUser authUser = TestUtil.authUser(userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(deleteAccountRequestDto.getPassword(), user.getPassword())).willReturn(false);

        WrongPasswordException exception = assertThrows(WrongPasswordException.class,
                () -> authService.deleteAccount(token, authUser, deleteAccountRequestDto)
        );

        assertEquals("잘못된 비밀번호입니다.", exception.getMessage());
    }

    @Test
    public void 회원탈퇴에_성공한다() {
        long userId = 1L;
        String token = "token";
        DeleteAccountRequestDto deleteAccountRequestDto = new DeleteAccountRequestDto("1234");
        User user = TestUtil.getUser(userId);
        AuthUser authUser = TestUtil.authUser(userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(deleteAccountRequestDto.getPassword(), user.getPassword())).willReturn(true);

        authService.deleteAccount(token, authUser, deleteAccountRequestDto);
        verify(blacklistTokenService, times(1)).addBlacklistToken(any());
    }
}
