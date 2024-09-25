package com.sparta.orderapp.service;

import com.sparta.orderapp.entity.BlackListToken;
import com.sparta.orderapp.repository.BlacklistTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BlacklistTokenService {
    private final BlacklistTokenRepository blacklistTokenRepository;

    /**
     * DB에 회원탈퇴한 유저의 Token을 추가하는 메서드
     * @param token 탈퇴한 회원의 Token값
     */
    @Transactional
    public void addBlacklistToken(String token) {
        BlackListToken blacklistToken = new BlackListToken(token);

        blacklistTokenRepository.save(blacklistToken);
    }

    /**
     * BlackListToken에 해당 token이 들어있는지 확인하는 메서드
     * @param token 토큰 값
     * @return token이 존재할 경우 반환 / 없을 경우 null 반환
     */
    @Transactional(readOnly = true)
    public BlackListToken findBlacklistToken(String token) {
        Optional<BlackListToken> foundToken = blacklistTokenRepository.findById(token);

        return foundToken.orElse(null);
    }
}
