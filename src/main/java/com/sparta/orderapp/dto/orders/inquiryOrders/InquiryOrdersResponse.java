package com.sparta.orderapp.dto.orders.inquiryOrders;

import com.sparta.orderapp.entity.Orders;
import lombok.Getter;

@Getter
public class InquiryOrdersResponse {
    private Long orderId;
    private Long userId;
    private String menuName;
    private int price;
    private int orderStatus;

    //    private String userName;
    //    private String shopName;

    public InquiryOrdersResponse(Orders orders){
        this.orderId=orders.getOrderId();
        this.userId=orders.getUser().getId();
        this.menuName=orders.getMenu().getName();
        this.price=orders.getMenu().getPrice();
        this.orderStatus=orders.getOrderStatus();
    }



}
