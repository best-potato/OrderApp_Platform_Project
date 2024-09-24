package com.sparta.orderapp.controller;

import com.sparta.orderapp.dto.shop.ShopResponseDto;
import com.sparta.orderapp.dto.shop.ShopSingleRetrievalResponseDto;
import com.sparta.orderapp.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// user 로그인시 사용하는 Api입니다.
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ShopController {
    private final ShopService shopService;


    // 가게 단건 조회
    @GetMapping("/users/shops/{shopId}")
    public ShopSingleRetrievalResponseDto getShop(@PathVariable Long shopId) {
        return shopService.getShop(shopId);
    }

    /***
     * @param page : 조회할 페이지 숫자
     * @param size : 전체 페이징 사이즈
     * @return 가게 다건 조회
     */
    @GetMapping("/users/shops")
    public ResponseEntity<List<ShopResponseDto>> getOpenShops(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {

        // 결과 반환 (Page 형식 그대로 반환)
        return ResponseEntity.ok(shopService.getOpenShops(page - 1, size));
    }

}
