package com.sparta.orderapp.dto.menu;

import lombok.Getter;

@Getter
public class UpdateMenuResponseDto {
    private Long menuId;
    private String name;
    private int price;
    private String message;


    public UpdateMenuResponseDto(Long menuId, String name, int price, String message) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.message = message;
    }
}
