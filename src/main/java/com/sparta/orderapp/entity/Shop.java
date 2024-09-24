package com.sparta.orderapp.entity;

import com.sparta.orderapp.dto.shop.ShopRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "shop")
public class Shop extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;

    @Column(unique = true, nullable = false, length = 20)
    private String shopName;
    @Column(nullable = false, length = 20)
    private String openTime;
    @Column(nullable = false, length = 20)
    private String closeTime;
    @Column(nullable = false)
    private int minOrderPrice;
    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    private ShopStatus shopStatus = ShopStatus.OPEN;  // Enum 타입으로 변경, 필드명도 소문자로 변경

    @OneToMany(mappedBy = "shop", cascade = CascadeType.PERSIST)
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY) //LAZY로 변경, 메뉴 목록은 필요할 때 별도의 쿼리로 가져옵니다.
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.PERSIST)
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
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
        this.shopStatus = ShopStatus.OPEN; // 정상영업 : 1
        this.shopName = shopRequestDto.getShopName();
        this.minOrderPrice = shopRequestDto.getMinOrderPrice();
    }

    public void update(ShopRequestDto requestDto) {
        this.closeTime = requestDto.getCloseTime();
        this.openTime = requestDto.getOpenTime();
        this.minOrderPrice = requestDto.getMinOrderPrice();
        this.shopName = requestDto.getShopName();
    }

    // 상태 변경 메서드
    public void setShopStatus(ShopStatus status) {
        this.shopStatus = status;
    }

}
