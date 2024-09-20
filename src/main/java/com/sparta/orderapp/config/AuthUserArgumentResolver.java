package com.sparta.orderapp.config;

import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.entity.User;
import com.sparta.orderapp.entity.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * AuthUser Annotation이 이미 존재하는지 확인
     * @param parameter MethodParameter 객체
     * @return true : 이미 AuthUser Annotation이 존재 / false : AuthUser Annotation이 존재하지 않음
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Auth.class) != null;
    }

    /**
     * Argument에 들어갈 데이터를 조립해주는 메서드
     */
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        assert request != null;

        long userId = (long)request.getAttribute("userId");
        String name = (String) request.getAttribute("name");
        UserRole userRole = UserRole.of((String) request.getAttribute("userRole"));

        return AuthUser.builder()
                .name(name)
                .id(userId)
                .userRole(userRole)
                .build();
    }
}
