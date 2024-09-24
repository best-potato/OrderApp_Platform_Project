package com.sparta.orderapp.service;

import com.sparta.orderapp.dto.orders.acceptOrders.AcceptOrdersRequest;
import com.sparta.orderapp.dto.orders.inquiryOrders.InquiryOrdersResponse;
import com.sparta.orderapp.dto.orders.postOrders.PostOrdersRequest;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.entity.Orders;
import com.sparta.orderapp.entity.UserRole;
import com.sparta.orderapp.exception.BadRequestException;
import com.sparta.orderapp.exception.ForbiddenException;
import com.sparta.orderapp.exception.NoSignedUserException;
import com.sparta.orderapp.repository.OrdersRepository;
import com.sparta.orderapp.repository.PopularShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final PopularShopRepository popularShopRepository;


    // 주문 생성
    public void postOrder(PostOrdersRequest reqDto, AuthUser authUser){
        if(reqDto.getMenuId()==null || reqDto.getShopId()==null){
            throw new BadRequestException("메뉴 또는 가게 id를 입력하세요.");
        }
        Orders orders = new Orders(reqDto, authUser.getId());
        ordersRepository.save(orders);
    }

    // 주문 조회
    // 본인의 주문만 조회 가능
    public InquiryOrdersResponse inquiryOrders(Long ordersId, AuthUser authUser){
        Orders orders =ordersRepository.findById(ordersId).orElseThrow(() -> new BadRequestException("해당 주문을 찾을 수 없습니다."));

        // 본인의 주문이 아닌 경우
        if(orders.getUser().getId() != authUser.getId()){
            throw new ForbiddenException("권한이 없습니다. : 타인의 주문");
        }

        return new InquiryOrdersResponse(orders);
    }


    // 주문 상태 변경
    public void changeOrdersStatus(Long ordersId, AuthUser authUser, AcceptOrdersRequest reqDto){
        if(reqDto.getOrdersStatus()==null){
            throw new BadRequestException("상태를 입력하세요.");
        }

        // 일반 유저가 주문 상태 변경을 하려는 경우
        if(!authUser.getUserRole().equals(UserRole.OWNER)){
            throw new ForbiddenException("권한이 없습니다. : 일반 사용자");
        }

        Orders orders =ordersRepository.findById(ordersId).orElseThrow(() -> new BadRequestException("해당 주문을 찾을 수 없습니다."));

        // 본인 가게의 주문이 아닌 경우
        if(orders.getShop().getOwner().getId() != authUser.getId()){
            throw new ForbiddenException("권한이 없습니다. : 타인의 가게");
        }

        orders.changeOrderStatus(reqDto.getOrdersStatus());
        ordersRepository.save(orders);

        if(reqDto.getOrdersStatus() == 4){
            popularShopRepository.incrementScore(orders.getShop().getShopId());
        }
    }

}
