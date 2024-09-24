package com.sparta.orderapp.service.orderservice;

import com.sparta.orderapp.TestUtil;
import com.sparta.orderapp.dto.orders.postOrders.PostOrdersRequest;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.repository.OrdersRepository;
import com.sparta.orderapp.repository.PopularShopRepository;
import com.sparta.orderapp.service.OrdersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class OrderServiceTest {
    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private PopularShopRepository popularShopRepository;
    @InjectMocks
    private OrdersService ordersService;

    @Test
    public void 주문생성에_성공한다() {
        long userId = 1L;
        PostOrdersRequest ordersRequest = new PostOrdersRequest(1L, 3L);
        AuthUser authUser = TestUtil.authUser(userId);

        ordersService.postOrder(ordersRequest, authUser);

        verify(ordersRepository, times(1)).save(any());
    }
}
