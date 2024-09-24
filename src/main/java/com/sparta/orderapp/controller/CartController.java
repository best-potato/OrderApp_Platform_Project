package com.sparta.orderapp.controller;

import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.dto.cart.CartCache;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니에 메뉴들을 담는 메서드
     * @param authUser 현재 로그인중인 유저에 대한 정보
     * @param shopId 장바구니를 사용할 Shop Id
     * @param menuIds 장바구니에 담을 Menu Id들
     * @return 장바구니에 대한 정보가 담긴 CartCache 객체
     */
    @PostMapping("/shops/{shopId}")
    public CartCache addItemToCart(@Auth AuthUser authUser, @PathVariable Long shopId, @RequestBody List<Long> menuIds) {
        return cartService.addItemToCart(authUser, shopId, menuIds);
    }

    /**
     * 장바구니에 있는 메뉴를 확인하는 메서드
     * @param authUser 현재 로그인중인 유저에 대한 정보
     * @return 장바구니에 담긴 메뉴들
     */
    @GetMapping
    public List<Object> getCartItems(@Auth AuthUser authUser) {
        return cartService.getCartItems(authUser);
    }

    /**
     * 장바구니를 비우는 메서드
     * @param authUser 현재 로그인중인 유저에 대한 정보
     */
    @DeleteMapping
    public void clearCart(@Auth AuthUser authUser) {
        cartService.clearCart(authUser);
    }


}
