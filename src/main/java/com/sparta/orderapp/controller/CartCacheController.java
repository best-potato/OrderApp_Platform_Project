package com.sparta.orderapp.controller;

import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.dto.cart.CartCache;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.service.CartCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartCacheController {

    private final CartCacheService cartCacheService;

    // 장바구니 생성
    @PostMapping("/shops/{shopId}")
    public CartCache addItemToCart(@Auth AuthUser authUser, @PathVariable Long shopId, @RequestBody List<Long> menuIds) {
        return cartCacheService.addItemToCart(authUser, shopId, menuIds);
    }

    // 장바구니 조회
    @GetMapping
    public List<Object> getCartItems(@Auth AuthUser authUser) {
        return cartCacheService.getCartItems(authUser);
    }

    // 장바구니 삭제
    @DeleteMapping
    public void clearCart(@Auth AuthUser authUser) {
        cartCacheService.clearCart(authUser);
    }


}
