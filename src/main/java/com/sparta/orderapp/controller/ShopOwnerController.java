package com.sparta.orderapp.controller;

import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.dto.shop.ShopRequestDto;
import com.sparta.orderapp.dto.shop.ShopResponseDto;
import com.sparta.orderapp.dto.shop.ShopSingleRetrievalResponseDto;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.entity.Shop;
import com.sparta.orderapp.exception.BadRequestException;
import com.sparta.orderapp.exception.GlobalExceptionHandler;
import com.sparta.orderapp.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// owner 로그인시 사용하는 Api입니다.
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ShopOwnerController {
    private final ShopService shopService;

    // 가게 생성 (사장님 전용)
    @PostMapping("/owners/shops")
    public ResponseEntity<ShopResponseDto> createShop(@Auth AuthUser authUser, @Valid @RequestBody ShopRequestDto requestDto) {
        // Step 1: 사장님의 현재 가게 수를 조회
        int shopCount = shopService.getActiveShopCount(authUser.getId());

        // Step 2: 가게 수가 4개 이상이면 예외 처리
        if (shopCount >= 3) {
            throw new BadRequestException("가게는 최대 3개까지만 운영할 수 있습니다.");
        }

        // Step 3: 가게 생성
        ShopResponseDto createdShop = shopService.createShop(authUser, requestDto);
        return ResponseEntity.ok(createdShop);
    }



    // 가게 수정 (사장님 전용)
    @PutMapping("/owners/shops/{shopId}") //반환타입 메서드
    public ShopResponseDto updateUser(@Auth AuthUser authUser, @PathVariable Long shopId, @RequestBody ShopRequestDto requestDto) {
        return shopService.updateShop(authUser, shopId, requestDto);
    }

    // 가게 다건조회 (유저랑 동일)
    @GetMapping("/owners/shops")
    public ResponseEntity<List<ShopResponseDto>> getShops(@RequestParam(defaultValue = "1", required = false) int page,
                                                          @RequestParam(defaultValue = "10", required = false) int size) {

        return ResponseEntity.ok(shopService.getOpenShops(page-1, size));
    }

    // 가게 단건조회 (유저랑 동일)
    @GetMapping("/owners/shops/{shopId}")
    public ShopSingleRetrievalResponseDto getShop(@PathVariable Long shopId) {
        return shopService.getShop(shopId);
    }

    // 가게 폐업 (사장님 전용)
    @PatchMapping("/owners/shops/{shopId}/close")
    public ResponseEntity<String> closeShop(@Auth AuthUser authUser, @PathVariable Long shopId) {
        // Step 1: 가게 정보 조회

        // Step 2: 가게 상태 변경 (폐업 상태로 변경) //200
        shopService.closeShop(shopId, authUser.getId());

        return ResponseEntity.ok("가게가 폐업 처리되었습니다.");
    }

}

// http://localhost:8080/api/users/kakao