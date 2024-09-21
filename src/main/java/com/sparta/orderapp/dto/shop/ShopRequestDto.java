package com.sparta.orderapp.dto.shop;

import lombok.Getter;

@Getter
public class ShopRequestDto{
    private String shopName; // 가게이름
    private String openTime; // 가게 여는시간
    private String closeTime; // 가게 마감시간
    private int minOrderPrice; // 최소 주문가격

}
