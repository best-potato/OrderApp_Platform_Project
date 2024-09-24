package com.sparta.orderapp.dto.popular;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopularShopResponse {
    Long shopId;
    String shopName;

    public PopularShopResponse(Long shopId, String shopName) {
        this.shopId = shopId;
        this.shopName = shopName;
    }
}
