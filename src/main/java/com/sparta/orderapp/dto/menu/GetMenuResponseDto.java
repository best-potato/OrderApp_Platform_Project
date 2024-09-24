package com.sparta.orderapp.dto.menu;


import com.sparta.orderapp.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetMenuResponseDto {
    private Long menuId;
    private String name;
    private int price;

    public GetMenuResponseDto(Menu menu) {
        this.menuId = menu.getMenuId();
        this.name = menu.getName();
        this.price = menu.getPrice();
    }

}

