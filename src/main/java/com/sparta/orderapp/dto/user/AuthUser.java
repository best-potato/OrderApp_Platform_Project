package com.sparta.orderapp.dto.user;

import com.sparta.orderapp.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class AuthUser {
    long id;
    String name;
    UserRole userRole;
}