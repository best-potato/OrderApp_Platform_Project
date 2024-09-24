package com.sparta.orderapp.config.utility;

import com.sparta.orderapp.config.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class KakaoConnetUtility {
    private final String USER_INFO_URI_STRING = "https://kapi.kakao.com";
    private final String USER_INFO_PATH = "/v2/user/me";

    private final String TOKEN_URI_STRING = "https://kauth.kakao.com";
    private final String TOKEN_URI_PATH = "/oauth/token";

    private final String REDIRECT_URI_BASE = "http://localhost:8080/api/";
    private final String REDIRECT_ROLE_PATH_USER = "users/kakao";
    private final String REDIRECT_ROLE_PATH_OWNER = "owners/kakao";

    @Value("${client_id}")
    private String clientId;

    public RequestEntity<MultiValueMap<String, String>> getUserInfoRequestEntity(String accessToken) {
        URI uri = getUserInfoURI();

        HttpHeaders headers = getContentHeader();
        headers.add("Authorization", JwtUtil.BEARER_PREFIX + accessToken);

        return RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());
    }

    private RequestEntity<MultiValueMap<String, String>> getTokenRequestEntity(String redirectUri, String code) {
        URI uri = getTokenURI();
        HttpHeaders headers = getContentHeader();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", clientId);
        map.add("redirect_uri", redirectUri);
        map.add("code", code);

       return RequestEntity
                .post(uri)
                .headers(headers)
                .body(map);
    }

    public RequestEntity<MultiValueMap<String, String>> getUserTokenRequestEntity(String code) {
        return getTokenRequestEntity(REDIRECT_URI_BASE + REDIRECT_ROLE_PATH_USER, code);
    }

    public RequestEntity<MultiValueMap<String, String>> getOwnerTokenRequestEntity(String code) {
        return getTokenRequestEntity(REDIRECT_URI_BASE + REDIRECT_ROLE_PATH_OWNER, code);
    }

    public URI getUserInfoURI() {
        return getKakaoUri(USER_INFO_URI_STRING, USER_INFO_PATH);
    }

    public URI getTokenURI() {
        return getKakaoUri(TOKEN_URI_STRING, TOKEN_URI_PATH);
    }

    private HttpHeaders getContentHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    private URI getKakaoUri(String uriString, String path) {
        return UriComponentsBuilder
                .fromUriString(uriString)
                .path(path)
                .encode()
                .build()
                .toUri();
    }
}
