package com.sparta.orderapp.dto.shop;

import com.sparta.orderapp.dto.menu.GetMenuResponseDto;
import com.sparta.orderapp.entity.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShopSingleRetrievalResponseDto {
    private Long shopId;
    private Long userId;
    private String shopName; // name -> shopName으로 바꾸겠습니다()
    private String openTime;
    private String closeTime;
    private int minOrderPrice;
    private List<GetMenuResponseDto> menus;


    public ShopSingleRetrievalResponseDto(Shop shop) {
        this.shopId = shop.getShopId();
        this.shopName = shop.getShopName();
        this.openTime = shop.getOpenTime();
        this.closeTime = shop.getCloseTime();
        this.minOrderPrice = shop.getMinOrderPrice();
        this.userId = shop.getOwner().getId();
        this.menus = shop.getMenus().stream().map(GetMenuResponseDto::new).toList(); // shop에서 menu 불러 올 수 있는 이유 : menu를 join했기 때문에, shop에 menu가 이미 들어가있다!
//                .map(GetMenuResponseDto::new)
//                .collect(Collectors.toList());
    }

}
