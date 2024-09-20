package com.sparta.orderapp.controller;

import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.dto.orders.PostOrdersRequest;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    // 주문 생성
    // 예외처리, 메서드 설명 추가 필요
    // 명세서에는 메뉴에 대한 정보를 반환하게 하는데, 필요한지에 대해서 의논 필요
    @PostMapping("/orders")
    public ResponseEntity<String> postOrder(@RequestBody PostOrdersRequest reqDto, @Auth AuthUser authUser) {
        ordersService.postOrder(reqDto, authUser);
        return ResponseEntity.ok().body("주문 완료");
    };

    // 주문 조회


    // 주문 수락

}
