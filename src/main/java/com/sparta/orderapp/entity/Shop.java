package com.sparta.orderapp.entity;

import com.sparta.orderapp.dto.shop.ShopRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "shop")
public class Shop extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;

    @Column(nullable = false, length = 20)
    private String shopName;
    @Column(nullable = false, length = 20)
    private String openTime;
    @Column(nullable = false, length = 20)
    private String closeTime;
    @Column(nullable = false)
    private int minOrderPrice;
    private boolean status;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.PERSIST)
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.PERSIST)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.PERSIST)
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // user_id -> owner_id
    private User owner; // 사장님

    public Shop(Long id){
        this.shopId=id;
    }
    // 생성자안에 @OnetoMany 컬럼은 넣을필요 없다.
    // autoincrement 컬럼도 넣을필요 없다.
    public Shop(User user, ShopRequestDto shopRequestDto) {
        this.closeTime = shopRequestDto.getCloseTime();
        this.openTime = shopRequestDto.getOpenTime();
        this.owner = user;
        this.status = true; // 정상영업 : 1
        this.shopName = shopRequestDto.getShopName();
        this.minOrderPrice = shopRequestDto.getMinOrderPrice();
    }

    public void update(ShopRequestDto requestDto) {
        this.closeTime = requestDto.getCloseTime();
        this.openTime = requestDto.getOpenTime();
        this.minOrderPrice = requestDto.getMinOrderPrice();
        this.shopName = requestDto.getShopName();
    }
}
