package com.sparta.orderapp.dto.shop;

import com.sparta.orderapp.entity.Shop;
import lombok.Getter;

@Getter
public class ShopResponseDto {
    private Long shopId;
    private Long userId;
    private String shopName; // name -> shopName으로 바꾸겠습니다()
    private String openTime;
    private String closeTime;
    private int minOrderPrice;

    public ShopResponseDto(Shop shop) {
        this.shopId = shop.getShopId();
        this.shopName = shop.getShopName(); // 가게이름
        this.openTime = shop.getOpenTime();
        this.closeTime = shop.getCloseTime();
        this.minOrderPrice = shop.getMinOrderPrice();
        this.userId = shop.getOwner().getId(); //
    }
}
