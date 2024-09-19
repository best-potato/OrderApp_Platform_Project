package com.sparta.orderapp.dto.sign;

import com.sparta.orderapp.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignupResponseDto {

    private final Long id;
    private final String email;
    private final String name;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public SignupResponseDto(User savedUser) {
        this.id = savedUser.getId();
        this.email = savedUser.getEmail();
        this.name = savedUser.getName();
        this.createdAt = savedUser.getCreatedAt();
        this.modifiedAt = savedUser.getModifiedAt();
    }
}
