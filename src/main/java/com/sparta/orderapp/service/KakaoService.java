package com.sparta.orderapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderapp.config.JwtUtil;
import com.sparta.orderapp.config.utility.KakaoConnetUtility;
import com.sparta.orderapp.dto.user.KakaoUserDto;
import com.sparta.orderapp.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public String kakaoOwnerLogin(String code) throws JsonProcessingException {
        String accessToken = getOwnerToken(code);
        KakaoUserDto kakaoUserdto = getKakaoUserInfo(accessToken);
        kakaoUserdto.setUserRole("OWNER");

        User foundUser = authService.findByKakaoId(kakaoUserdto.getId());
        if (foundUser == null) {
            foundUser = authService.signUpUseKakao(kakaoUserdto);
        }

        return jwtUtil.createToken(foundUser);
    }

    public String kakaoUserLogin(String code) throws JsonProcessingException {
        String accessToken = getUserToken(code);
        KakaoUserDto kakaoUserdto = getKakaoUserInfo(accessToken);
        kakaoUserdto.setUserRole("USER");

        User foundUser = findUserByKakaoIdIfNullSignUp(kakaoUserdto);
        return jwtUtil.createToken(foundUser);
    }

    private User findUserByKakaoIdIfNullSignUp(KakaoUserDto kakaoUserdto) throws JsonProcessingException {
        User foundUser = authService.findByKakaoId(kakaoUserdto.getId());

        if (foundUser == null) {
            foundUser = authService.signUpUseKakao(kakaoUserdto);
        }

        return foundUser;
    }

    private KakaoUserDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        URI uri = kakaoConnetUtility.getUserInfoURI();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + accessToken);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String kakaoId = jsonNode.get("id").asText();

        log.info(id + " " + nickname + " " + email);
        return new KakaoUserDto(id, nickname, email);
    }

    public String getOwnerToken(String code) throws JsonProcessingException {
        RequestEntity<MultiValueMap<String, String>> requestEntity = kakaoConnetUtility.getOwnerTokenRequestEntity(code);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        return parseAccessToken(requestEntity);
    }

    public String getUserToken(String code) throws JsonProcessingException {
        RequestEntity<MultiValueMap<String, String>> requestEntity = kakaoConnetUtility.getUserTokenRequestEntity(code);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        return parseAccessToken(requestEntity);
    }

    private String parseAccessToken(RequestEntity<MultiValueMap<String, String>> requestEntity) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }
}
