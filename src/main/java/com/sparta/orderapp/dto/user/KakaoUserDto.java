package com.sparta.orderapp.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoUserDto {
    private Long id;
    private String nickname;
    private String email;
    private String userRole;

    public KakaoUserDto(Long id, String nickname, String email, String userRole) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.userRole = userRole;
    }

    public KakaoUserDto(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}