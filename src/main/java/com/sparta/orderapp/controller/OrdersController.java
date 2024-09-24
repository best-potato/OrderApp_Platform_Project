package com.sparta.orderapp.controller;

import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.dto.orders.acceptOrders.AcceptOrdersRequest;
import com.sparta.orderapp.dto.orders.inquiryOrders.InquiryOrdersResponse;
import com.sparta.orderapp.dto.orders.postOrders.PostOrdersRequest;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.exception.BadRequestException;
import com.sparta.orderapp.exception.ForbiddenException;
import com.sparta.orderapp.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    // 주문 생성
    // 메서드 설명 추가 필요
    // 명세서에는 메뉴에 대한 정보를 반환하게 하는데, 필요한지에 대해서 의논 필요
    @PostMapping
    public ResponseEntity<String> postOrder(@RequestBody PostOrdersRequest reqDto, @Auth AuthUser authUser) {
        ordersService.postOrder(reqDto, authUser);
        return ResponseEntity.ok().body("주문 완료");
    };

    // 주문 조회
    @GetMapping("/{ordersId}")
    public ResponseEntity<InquiryOrdersResponse> inquiryOrders(@PathVariable Long ordersId, @Auth AuthUser authUser) {
        InquiryOrdersResponse resDto = ordersService.inquiryOrders(ordersId, authUser);
        return ResponseEntity.ok().body(resDto);
    }


    // reqDto에 valid 필요 : 0~6 제한
    @PutMapping("/{ordersId}/status")
    public ResponseEntity<String> changeOrdersStatus(@PathVariable Long ordersId,
                             @Auth AuthUser authUser,
                             @RequestBody AcceptOrdersRequest reqDto){
        ordersService.changeOrdersStatus(ordersId, authUser, reqDto);
        return ResponseEntity.ok().body("주문 상태 변경 : "+reqDto.getOrdersStatus());
    }


}
