package com.sparta.orderapp.config;

import com.sparta.orderapp.entity.UserRole;
import com.sparta.orderapp.service.BlacklistTokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

@Qualifier
@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {
    private final JwtUtil jwtUtil;
    private final BlacklistTokenService blacklistTokenService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        // 특정 URL & HTTP 메서드에 대해 필터를 건너뛰도록 설정
        if (isByPassFilter(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        String bearerJwt = httpRequest.getHeader("Authorization"); //key값
        if (bearerJwt == null || !bearerJwt.startsWith("Bearer ")) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 필요합니다.");
            return;
        }

        String jwt = jwtUtil.substringToken(bearerJwt);

        // 블랙리스트 토큰에 등록되어 있는 경우 거부
        if (isBlacklistToken(jwt)) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
            return;
        }

        try {
            // JWT 유효성 검사와 claims 추출
            Claims claims = jwtUtil.extractClaims(jwt);

            long userId = Long.parseLong(claims.getSubject());
            String name = claims.get("name", String.class);
            String userRoleString = claims.get("userRole", String.class);
            UserRole userRole = UserRole.valueOf(userRoleString);

            // 사용자 정보를 ArgumentResolver 로 넘기기 위해 HttpServletRequest 에 세팅
            httpRequest.setAttribute("userId", userId);
            httpRequest.setAttribute("name", name);
            httpRequest.setAttribute("userRole", userRoleString);

            // owner 페이지에 접근 하지만 owner가 아닌 경우
            if (isOwnerPage(httpRequest) && !isOwner(userRole)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "권한이 존재하지 않습니다.");
                return;
            }

            if (jwtUtil.validateToken(jwt)) {
                chain.doFilter(request, response);
            } else {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다.");
            }
        } catch (Exception e) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰 검증 중 오류가 발생했습니다.");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    /**
     * 해당 Page가 사장님 전용 페이지인지 확인하는 메서드
     * @param request uri가 담긴 HttpServletRequest
     * @return True : 사장님 전용 페이지 / False : 유저 전용 or 공동 페이지
     */
    private boolean isOwnerPage(HttpServletRequest request) {
        String uri = request.getRequestURI();

        return uri.startsWith("/api/owner");
    }

    /**
     * 해당 userRole이 OWNER인지 확인하는 메서드
     * @param userRole 확인할 UserRole enum 객체
     * @return True : OWNER / False : USER
     */
    private boolean isOwner(UserRole userRole) {
        return UserRole.OWNER.equals(userRole);
    }

    /**
     * 필터를 스킵할 URL / HTTP 메서드인지 체크하는 메서드
     * @param request 확인할 HttpServletRequest 객체
     * @return True : JWT를 확인하지 않는 URL / False : JWT인증이 필요한 URL
     */
    public boolean isByPassFilter(HttpServletRequest request) {
        String url = request.getRequestURI();
        // 유저 관련 Post 메서드와 shop 관련 Get 메서드는 Jwt 인증 없이도 동작해야 한다.
        return (request.getMethod().equals("POST") && url.startsWith("/api/users"))
                || (request.getMethod().equals("GET") && url.startsWith("/api/shops"));
    }

    /**
     * 해당 토큰이 BlacklistToken에 추가되어 있는지 확인하는 메서드
     * @param token 확인할 토큰값
     * @return True : 이미 블랙리스트에 등록된 토큰 / False : 사용 가능한 토큰
     */
    private boolean isBlacklistToken(String token) {
        return blacklistTokenService.findBlacklistToken(token) != null;
    }
}
