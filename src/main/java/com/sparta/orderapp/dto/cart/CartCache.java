package com.sparta.orderapp.dto.cart;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class CartCache implements Serializable {
    private Long userId;
    private Long shopId;
    private List<Long> menuIds;

    public CartCache(long id, Long shopId, List<Long> menuIds) {
        this.userId = id;
        this.shopId = shopId;
        this.menuIds = menuIds;
    }
}
