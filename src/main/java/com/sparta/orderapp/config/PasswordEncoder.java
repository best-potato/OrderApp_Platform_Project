package com.sparta.orderapp.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {
    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    /**
     * 평문 형태의 비빌번호와 암호화된 비밀번호가 동일한지 확인하는 메서드
     * @param rawPassword 평문 형태의 비밀번호
     * @param encodedPassword 암호화가 진행된 비밀번호
     * @return True : 두 비밀번호가 일치한다 / False : 두 비밀번호가 일치하지 않는다.
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}
