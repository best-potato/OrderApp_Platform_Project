package com.sparta.orderapp.entity;

import com.sparta.orderapp.dto.menu.MenuRequestDto;
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
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private Boolean status = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<Orders> orders = new ArrayList<>();

    public Menu(Long id){
        this.orderId=id;
    }

    public Menu(MenuRequestDto requestDto, Shop shop) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.shop = shop;
    }

    public void menuUpdate(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
