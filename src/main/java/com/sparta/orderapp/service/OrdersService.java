package com.sparta.orderapp.service;

import com.sparta.orderapp.dto.orders.PostOrdersRequest;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.entity.Orders;
import com.sparta.orderapp.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository ordersRepository;


    // 주문 생성
    public void postOrder(PostOrdersRequest reqDto, AuthUser authUser){
        Orders orders = new Orders(reqDto, authUser.getId());
        ordersRepository.save(orders);
    }

    // 주문 조회


    // 주문 수락

}
