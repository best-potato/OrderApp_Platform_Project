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
    // 유저 정보를 불러오는 시작 URI
    private final String USER_INFO_URI_STRING = "https://kapi.kakao.com";
    // 유저 정보를 불러오는 Path URI
    private final String USER_INFO_PATH = "/v2/user/me";

    // Token을 받아오는 시작 URI
    private final String TOKEN_URI_STRING = "https://kauth.kakao.com";
    // Token을 받아오는 Path URI
    private final String TOKEN_URI_PATH = "/oauth/token";

    // Kakao에 보내야 하는 Redirect 시작 URI
    private final String REDIRECT_URI_BASE = "http://localhost:8080/api/";
    // Kakao에 보내야 하는 Redirect Path URI
    private final String REDIRECT_ROLE_PATH_USER = "users/kakao";
    private final String REDIRECT_ROLE_PATH_OWNER = "owners/kakao";

    // Client Id ( 인증키 )
    @Value("${client_id}")
    private String clientId;

    /**
     * 유저 정보를 얻어내기 위한 Request를 조립하는 메서드
     * @param accessToken 정보를 얻을 유저의 accessToken
     * @return 조립된 RequestEntity
     */
    public RequestEntity<MultiValueMap<String, String>> getUserInfoRequestEntity(String accessToken) {
        URI uri = getUserInfoURI();

        HttpHeaders headers = getContentHeader();
        headers.add("Authorization", JwtUtil.BEARER_PREFIX + accessToken);

        return RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());
    }

    /**
     * 토큰을 받아내기 위한 Request를 조립하는 메서드
     * @param redirectUri 카카오에 제공할 Redirect URI 정보 ( 카카오에 로그인 요청할 때 Path에 넣는 내용과 동일해야 한다 )
     * @param code 카카오에서 반환한 로그인 Code
     * @return 조립된 RequestEntity
     */
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

    /**
     * User의 Token을 얻기 위한 RequestEntity를 반환하는 메서드
     * @param code kakao에서 보낸 로그인 코드
     * @return 해당 유저의 권한에 맞는 RequestEntity
     */
    public RequestEntity<MultiValueMap<String, String>> getUserTokenRequestEntity(String code) {
        return getTokenRequestEntity(REDIRECT_URI_BASE + REDIRECT_ROLE_PATH_USER, code);
    }

    /**
     * Owner의 Token을 얻기 위한 RequestEntity를 반환하는 메서드
     * @param code kakao에서 보낸 로그인 코드
     * @return 해당 사장님에 맞는 RequestEntity
     */
    public RequestEntity<MultiValueMap<String, String>> getOwnerTokenRequestEntity(String code) {
        return getTokenRequestEntity(REDIRECT_URI_BASE + REDIRECT_ROLE_PATH_OWNER, code);
    }

    /**
     * User의 정보를 조회할 URI를 반환하는 메서드
     * @return 유저 정보를 조회할 URI
     */
    public URI getUserInfoURI() {
        return getKakaoUri(USER_INFO_URI_STRING, USER_INFO_PATH);
    }

    /**
     * Token을 전해 받을 URI를 반환하는 메서드
     * @return 해당 유저의 유저 정보를 조회할 URI
     */
    public URI getTokenURI() {
        return getKakaoUri(TOKEN_URI_STRING, TOKEN_URI_PATH);
    }

    /**
     * Content에 대한 정보가 담긴 HttpHeader를 반환하는 메서드
     * @return Content에 대한 정보가 담긴 HttpHeader
     */
    private HttpHeaders getContentHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    /**
     * UriString과 Path를 통해 URI를 조립하여 반환하는 메서드
     * @param uriString 시작 URI
     * @param path URI 패스
     * @return 조립된 URI
     */
    private URI getKakaoUri(String uriString, String path) {
        return UriComponentsBuilder
                .fromUriString(uriString)
                .path(path)
                .encode()
                .build()
                .toUri();
    }
}
