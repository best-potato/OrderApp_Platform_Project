package com.sparta.orderapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {
    private final JwtUtil jwtUtil;


    /**
     * Jwt 필터를 등록하는 메서드
     * @return 필터가 등록된 FilterRegistrationBean객체
     */
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter(jwtUtil));
        registrationBean.addUrlPatterns("/*"); // 필터를 적용할 URL 패턴을 지정합니다.

        return registrationBean;
    }

    /**
     * 필터 적용할 url을 적용하는 메서드 / 현재는 모든 URL 오픈
     * @param http URL 필터를 적용할 http Security
     * @return 필터가 적용된 HttpSecurity 객체
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((AbstractHttpConfigurer::disable));

        // 접근 권한 설정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // 유저 관련 승인
                        .requestMatchers("/**").permitAll()
                        // 그 외에는 인증이 되어야 한다.
                        .anyRequest().authenticated()
        );

        return http.build();
    }
}

