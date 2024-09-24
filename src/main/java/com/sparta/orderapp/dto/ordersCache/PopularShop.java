package com.sparta.orderapp.dto.ordersCache;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PopularShop implements Serializable {
    private Long shopId;
}
