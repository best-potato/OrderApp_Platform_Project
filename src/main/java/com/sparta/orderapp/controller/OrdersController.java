package com.sparta.orderapp.controller;

import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.dto.orders.acceptOrders.AcceptOrdersRequest;
import com.sparta.orderapp.dto.orders.inquiryOrders.InquiryOrdersResponse;
import com.sparta.orderapp.dto.orders.postOrders.PostOrdersRequest;
import com.sparta.orderapp.dto.user.AuthUser;
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
    // 예외처리, 메서드 설명 추가 필요
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


    // 주문 수락 -> 주문 상태 변경으로 바꾸면 어떨지?
    // 해당 가게 사장이 수락을 눌러야 함
    // reqDto에 valid 필요 : 0~6 제한
    @PutMapping("/{ordersId}/status")
    public ResponseEntity<String> changeOrdersStatus(@PathVariable Long ordersId,
                             @Auth AuthUser authUser,
                             @RequestBody AcceptOrdersRequest reqDto){
        int result = ordersService.changeOrdersStatus(ordersId, authUser, reqDto);
        if(result == 0){
            return ResponseEntity.status(403).body("본인의 가게가 아닙니다.");
        };
        return ResponseEntity.ok().body("주문 상태 변경 : "+reqDto.getOrdersStatus());
    }


}
