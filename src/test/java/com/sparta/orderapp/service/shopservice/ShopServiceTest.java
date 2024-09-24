package com.sparta.orderapp.service.shopservice;

import com.sparta.orderapp.TestUtil;
import com.sparta.orderapp.dto.shop.ShopRequestDto;
import com.sparta.orderapp.dto.shop.ShopResponseDto;
import com.sparta.orderapp.dto.shop.ShopSingleRetrievalResponseDto;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.entity.Shop;
import com.sparta.orderapp.entity.User;
import com.sparta.orderapp.repository.ShopRepository;
import com.sparta.orderapp.repository.UserRepository;
import com.sparta.orderapp.service.CommonService;
import com.sparta.orderapp.service.ShopService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class ShopServiceTest {
    @Mock
    private ShopRepository shopRepository;
    @Mock
    private CommonService commonService;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ShopService shopService;


    @Test
    public void 상점_추가시_유저가_존재하지_않는다면_RE를_반환한다() {
        long userId = 1L;
        ShopRequestDto shopRequestDto = new ShopRequestDto("a", "b", "c", 100);
        AuthUser authUser = TestUtil.authUser(userId);

        given(userRepository.findById(authUser.getId())).willReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> shopService.createShop(authUser, shopRequestDto));

        assertEquals("해당 유저가 없습니다.", exception.getMessage());
    }

    @Test
    public void 상점_추가에_성공한다() {
        long userId = 1L;
        long shopId = 1L;
        ShopRequestDto shopRequestDto = new ShopRequestDto("a", "b", "c", 100);
        AuthUser authUser = TestUtil.authUser(userId);
        User user = TestUtil.getUser(userId);
        Shop shop = TestUtil.getShop(shopId);

        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(user));
        given(shopRepository.save(any())).willReturn(shop);

        ShopResponseDto dto = shopService.createShop(authUser, shopRequestDto);

        assertNotNull(dto);
    }

    @Test
    public void 상점정보_갱신에_성공한다() {
        long userId = 1L;
        long shopId = 1L;
        ShopRequestDto shopRequestDto = new ShopRequestDto("a", "b", "c", 100);
        AuthUser authUser = TestUtil.authUser(userId);
        User user = TestUtil.getUser(userId);
        Shop shop = TestUtil.getShop(shopId);


        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(user));
        given(commonService.findShop(any())).willReturn(shop);
        given(shopRepository.save(any())).willReturn(shop);

        ShopResponseDto dto = shopService.updateShop(authUser, shopId, shopRequestDto);

        assertNotNull(dto);
    }

    @Test
    public void 상점을_폐업시킬시_해당_상점이_존재하지_않으면_IAE를_반환한다() {
        long userId = 1L;
        long shopId = 1L;

        given(shopRepository.findByShopIdAndOwnerId(any(), any())).willReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> shopService.closeShop(shopId, userId));

        assertEquals("가게를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    public void 상점_단건조회시_해당_상점이_존재하지_않으면_IAE를_반환한다() {
        long shopId = 1L;

        given(shopRepository.findByShopId(any())).willReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> shopService.getShop(shopId)
        );

        assertEquals("가게를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    public void 상점_단건조회에_성공한다() {
        long shopId = 1L;
        Shop shop = TestUtil.getShop(shopId);

        given(shopRepository.findByShopId(any())).willReturn(Optional.of(shop));

        ShopSingleRetrievalResponseDto dto = shopService.getShop(shopId);

        assertNotNull(dto);
    }

    @Test
    public void 영업중인_가게_수_카운팅에_성공한다() {
        long userId = 1L;

        given(shopRepository.countByOwnerIdAndShopStatus(any(), any())).willReturn(2);
        int result = shopService.getActiveShopCount(userId);
        assertEquals(2, result);
    }

    @Test
    public void 상점을_폐업에_성공한다() {
        long userId = 1L;
        long shopId = 1L;
        Shop shop = TestUtil.getShop(shopId);
        shop = Mockito.mock(Shop.class);

        given(shopRepository.findByShopIdAndOwnerId(any(), any())).willReturn(Optional.of(shop));

        shopService.closeShop(shopId, userId);
        verify(shop, times(1)).setShopStatus(any());
    }

    @Test
    public void 상점_다건조회에_성공한다() {
        long userId = 1L;
        long shopId = 1L;
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);

        Shop shop = TestUtil.getShop(shopId);
        shop = Mockito.mock(Shop.class);

        List<Shop> shops = new ArrayList<>(List.of(
                TestUtil.getShop(shopId),
                TestUtil.getShop(shopId),
                TestUtil.getShop(shopId),
                TestUtil.getShop(shopId)));

        Page<Shop> shopPage = new PageImpl<>(shops.stream().toList());
        given(shopRepository.findAllByShopStatus(any(), any())).willReturn(shopPage);

        List<ShopResponseDto> dto = shopService.getOpenShops(page, size);
        assertNotNull(dto);
    }
}
