package com.sparta.orderapp.dto.orders.postOrders;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostOrdersRequest {
    Long menuId;
    Long shopId;
}
