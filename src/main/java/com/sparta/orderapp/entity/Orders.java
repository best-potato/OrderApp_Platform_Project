package com.sparta.orderapp.entity;

import com.sparta.orderapp.dto.orders.postOrders.PostOrdersRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Orders extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    // 피드백 받고 싶은 부분
    public Orders(PostOrdersRequest reqDto,Long userId){
        this.menu = new Menu(reqDto.getMenuId());
        this.shop = new Shop(reqDto.getShopId());
        this.user = new User(userId);
    }
    public void changeOrderStatus(int status){
        this.orderStatus = status;
    }

}
