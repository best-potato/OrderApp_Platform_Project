package com.sparta.orderapp.config;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/***
 * <JWT 관련 기능>
 * 1. JWT 생성
 * 2. 생성된 JWT를 Cookie에 저장
 * 3. Cookie에 들어있던 JWT 토큰을 Substring
 * 4. JWT 검증
 * 5. JWT에서 사용자 정보 가져오기
 */

@Component
@Slf4j(topic = "JwtUtil")
public class JwtUtil {
    //-------------토큰생성에 필요한 데이터-------------//
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }
    //-------------토큰생성에 필요한 데이터-------------//
    //-------------JWT 토큰 생성-------------//
    public String createToken(Long userId, String nickname, String email) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId)) // 사용자 식별자값(ID)
                        .claim("nickname", nickname) // 사용자 권한
                        .claim("email", email)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }
    //-------------JWT 토큰 생성-------------//

    // substringToken = 받아온 Cookie value//
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        log.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // validateToken //

    /*** <validateToken>
     *  StringUtils.hasText를 사용하여 공백,
     *  null을 확인하고 startsWith을 사용하여 토큰의 시작값이 Bearer이 맞는지 확인합니다.
     *  맞다면 순수 JWT를 반환하기 위해 substring을 사용하여 Bearer을 잘라냅니다.
     */
    public boolean validateToken(String token) {
        try {
            //parserBuildeer()로 JWT에서 사용자 정보 가져오기
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

