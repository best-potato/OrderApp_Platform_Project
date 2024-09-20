package com.sparta.orderapp.entity;

import com.sparta.orderapp.dto.menu.CreateMenuRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    private String name;
    private int price;

    @Column(nullable = false)
    private Boolean status = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<Order> orders = new ArrayList<>();


    public Menu(CreateMenuRequestDto requestDto, Shop shop) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.shop = shop;
    }

    public void menuUpdate(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
