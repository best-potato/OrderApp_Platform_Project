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

    // 장바구니 생성
    @PostMapping("/shops/{shopId}")
    public CartCache addItemToCart(@Auth AuthUser authUser, @PathVariable Long shopId, @RequestBody List<Long> menuIds) {
        return cartService.addItemToCart(authUser, shopId, menuIds);
    }

    // 장바구니 조회
    @GetMapping
    public List<Object> getCartItems(@Auth AuthUser authUser) {
        return cartService.getCartItems(authUser);
    }

    // 장바구니 삭제
    @DeleteMapping
    public void clearCart(@Auth AuthUser authUser) {
        cartService.clearCart(authUser);
    }


}
