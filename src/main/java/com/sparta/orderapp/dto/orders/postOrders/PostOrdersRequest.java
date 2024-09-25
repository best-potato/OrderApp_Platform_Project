package com.sparta.orderapp.dto.orders.postOrders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostOrdersRequest {
    Long menuId;
    Long shopId;
}
