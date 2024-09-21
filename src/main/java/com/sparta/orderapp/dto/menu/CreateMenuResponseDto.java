package com.sparta.orderapp.dto.menu;

import com.sparta.orderapp.entity.Menu;
import lombok.Getter;

@Getter
public class CreateMenuResponseDto {
    private Long menuId;
    private String name;
    private int price;

    public CreateMenuResponseDto(Menu menu) {
        this.menuId = menu.getMenuId();
        this.name = menu.getName();
        this.price = menu.getPrice();
    }
}
