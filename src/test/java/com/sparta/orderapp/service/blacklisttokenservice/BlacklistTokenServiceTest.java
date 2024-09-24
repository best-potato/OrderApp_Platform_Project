package com.sparta.orderapp.service.blacklisttokenservice;

import com.sparta.orderapp.entity.BlackListToken;
import com.sparta.orderapp.repository.BlacklistTokenRepository;
import com.sparta.orderapp.service.BlacklistTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class BlacklistTokenServiceTest {
    @InjectMocks
    BlacklistTokenService blacklistTokenService;

    @Mock
    BlacklistTokenRepository blacklistTokenRepository;

    @Test
    public void 블랙리스트_토큰이_성공적으로_등록된다() {
        String token = "token";

        blacklistTokenService.addBlacklistToken(token);

        verify(blacklistTokenRepository, times(1)).save(any());
    }

    @Test
    public void 블랙리스트_토큰_검색이_작동한다() {
        String token = "token";

        given(blacklistTokenRepository.findById(token)).willReturn(Optional.empty());
        BlackListToken foundToken = blacklistTokenService.findBlacklistToken(token);

        assertNull(foundToken);
    }
}
