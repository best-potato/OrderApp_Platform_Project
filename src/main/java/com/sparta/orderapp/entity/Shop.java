package com.sparta.orderapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String name;

    // 가게 하시는 분이 정하세요. 일단 String으로 만들게요.
    private String openTime;
    private String closeTime;

    private int minOrderAmount;

    private Boolean status;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.PERSIST)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.PERSIST)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.PERSIST)
    private List<Review> reviews = new ArrayList<>();

}
