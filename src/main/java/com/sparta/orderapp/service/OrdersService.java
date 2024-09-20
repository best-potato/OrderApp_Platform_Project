package com.sparta.orderapp.service;

import com.sparta.orderapp.dto.orders.acceptOrders.AcceptOrdersRequest;
import com.sparta.orderapp.dto.orders.inquiryOrders.InquiryOrdersResponse;
import com.sparta.orderapp.dto.orders.postOrders.PostOrdersRequest;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.entity.Orders;
import com.sparta.orderapp.entity.UserRole;
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
    // 본인의 주문만 조회 가능
    public InquiryOrdersResponse inquiryOrders(Long ordersId, AuthUser authUser){
        Orders orders =ordersRepository.findById(ordersId).orElse(null); // 에러 헨들러 구현 후, 예외 처리 예정
//        // 주문 당사자가 맞는 경우 예외 처리
//        if(orders.getUser().getId() != authUser.getId()){
//
//        }

        return new InquiryOrdersResponse(orders);
    }


    // 주문 상태 변경
    public int changeOrdersStatus(Long ordersId, AuthUser authUser, AcceptOrdersRequest reqDto){
        // 일반 유저가 주문 상태 변경을 하려는 경우
        if(!authUser.getUserRole().equals(UserRole.OWNER)){
            return 0; // 나중에 예외 처리
        }

        // 본인 가게의 주문이 아닌 경우
        Orders orders =ordersRepository.findById(ordersId).orElse(null);
        if(orders.getShop().getUser().getId() != authUser.getId()){
            return 0; // 나중에 예외 처리
        }

        orders.changeOrderStatus(reqDto.getOrdersStatus());
        ordersRepository.save(orders);
        return 1;
    }

}
