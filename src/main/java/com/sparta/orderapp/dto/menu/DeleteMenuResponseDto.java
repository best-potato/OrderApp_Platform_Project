package com.sparta.orderapp.dto.menu;

import lombok.Getter;

@Getter
public class DeleteMenuResponseDto {
    private final String message;

    public DeleteMenuResponseDto(String message) {
        this.message = message;
    }
}
