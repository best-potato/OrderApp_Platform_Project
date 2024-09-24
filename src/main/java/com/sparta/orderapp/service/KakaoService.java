package com.sparta.orderapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.sparta.orderapp.config.JwtUtil;
import com.sparta.orderapp.config.utility.KakaoConnetUtility;
import com.sparta.orderapp.dto.user.KakaoUserDto;
import com.sparta.orderapp.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {
    private final RestTemplate restTemplate;
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final KakaoConnetUtility kakaoConnetUtility;

    /**
     * 카카오를 통해 Owner계정을 로그인 하고 Access토큰을 받는 메서드
     * @param code 카카오에서 제공한 로그인 Code
     * @return 해당 유저의 AccessToken
     */
    public String kakaoOwnerLogin(String code) throws JsonProcessingException {
        String accessToken = getOwnerToken(code);
        KakaoUserDto kakaoUserdto = getKakaoUserInfo(accessToken);
        kakaoUserdto.setUserRole("OWNER");

        User foundUser = findUserByKakaoIdIfNullSignUp(kakaoUserdto);

        return jwtUtil.createToken(foundUser);
    }

    /**
     * 카카오를 통해 User계정을 로그인 하고 Access토큰을 받는 메서드
     * @param code 카카오에서 제공한 로그인 Code
     * @return 해당 유저의 AccessToken
     */
    public String kakaoUserLogin(String code) throws JsonProcessingException {
        String accessToken = getUserToken(code);
        KakaoUserDto kakaoUserdto = getKakaoUserInfo(accessToken);
        kakaoUserdto.setUserRole("USER");

        User foundUser = findUserByKakaoIdIfNullSignUp(kakaoUserdto);
        return jwtUtil.createToken(foundUser);
    }

    /**
     * 해당 유저가 이미 가입되어 있는지 확인하고, 가입되어 있지 않다면 회원가입을 진행하는 메서드
     * @param kakaoUserdto 카카오 유저 정보가 담긴 Dto
     * @return 로그인 / 가입된 User객체
     */
    private User findUserByKakaoIdIfNullSignUp(KakaoUserDto kakaoUserdto) throws JsonProcessingException {
        User foundUser = authService.findByKakaoId(kakaoUserdto.getId());

        if (foundUser == null) {
            foundUser = authService.signUpUseKakao(kakaoUserdto);
        }

        return foundUser;
    }

    /**
     * accessToken을 통해 유저 정보를 조회하는 메서드
     * @param accessToken 조회할 access토큰
     * @return 해당 유저 정보가 담긴 KakaoUserDto 객체
     */
    private KakaoUserDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        RequestEntity<MultiValueMap<String, String>> requestEntity = kakaoConnetUtility.getUserInfoRequestEntity(accessToken);
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        return parseKakaoUserInfo(requestEntity);
    }

    /**
     * 사장님 계정으로 로그인시 AccessToken을 반환하는 메서드
     * @param code kakao에서 제공한 로그인 Code
     * @return AccessToken
     */
    public String getOwnerToken(String code) throws JsonProcessingException {
        RequestEntity<MultiValueMap<String, String>> requestEntity = kakaoConnetUtility.getOwnerTokenRequestEntity(code);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        return parseAccessToken(requestEntity);
    }

    /**
     * 유저 계정으로 로그인시 AccessToken을 반환하는 메서드
     * @param code kakao에서 제공한 로그인 Code
     * @return AccessToken
     */
    public String getUserToken(String code) throws JsonProcessingException {
        RequestEntity<MultiValueMap<String, String>> requestEntity = kakaoConnetUtility.getUserTokenRequestEntity(code);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        return parseAccessToken(requestEntity);
    }

    /**
     * AccessToken 요청 결과 중 accessToken을 추출해내는 메서드
     * @param requestEntity 요청 내용에 대한 정보가 담긴 requestEntity
     * @return 추출한 AccessToken
     */
    private String parseAccessToken(RequestEntity<MultiValueMap<String, String>> requestEntity) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        return jsonNode.get("access_token").asText();
    }

    /**
     * UserInfo 요청 결과중 User 정보만 추출해내는 메서드
     * @param requestEntity 요청 내용에 대한 정보가 담긴 requestEntity
     * @return 유저 정보가 담긴 kakaoUserDto
     */
    private KakaoUserDto parseKakaoUserInfo(RequestEntity<MultiValueMap<String, String>> requestEntity) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();

        return new KakaoUserDto(id, nickname, email);
    }
}
