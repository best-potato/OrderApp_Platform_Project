package com.sparta.orderapp.service;

import com.sparta.orderapp.dto.shop.ShopRequestDto;
import com.sparta.orderapp.dto.shop.ShopResponseDto;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.entity.Shop;
import com.sparta.orderapp.entity.ShopStatus;
import com.sparta.orderapp.entity.User;
import com.sparta.orderapp.exception.NoSignedUserException;
import com.sparta.orderapp.repository.ShopRepository;
import com.sparta.orderapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {

    private final ShopRepository shopRepository;
    private final CommonService commonService;
    private final UserRepository userRepository;

//    public ShopService(ShopRepository shopRepository){
//        this.shopRepository = shopRepository;
//    }

    /***
     * Shop 만들기(사장님용)
     */
    @Transactional(readOnly = false)
    public ShopResponseDto createShop(AuthUser authUser, ShopRequestDto shopRequestDto) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(()-> new RuntimeException("해당 유저가 없습니다."));
        // RequestDto -> Entity
        Shop shop = new Shop(user, shopRequestDto);
        Shop saveShop = shopRepository.save(shop);

        // Entity -> ResponseDto
        return new ShopResponseDto(saveShop);
    }

    /***
     * Shop 수정하기(사장님용)
     */
    @Transactional
    public ShopResponseDto updateShop(AuthUser authUser, Long shopId, ShopRequestDto requestDto) {
        Shop shop = commonService.findShop(shopId);
        commonService.confirmCreator(authUser.getId(), shopId, false);
        shop.update(requestDto);
        Shop saveComment = shopRepository.save(shop);
        return new ShopResponseDto(saveComment);
    }

    /***
     * Shop 폐업처리(사장님용)
     */
    @Transactional
    public void closeShop(Long shopId, Long ownerId) {
        Shop shop = shopRepository.findByShopIdAndOwnerId(shopId, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));
        shop.setShopStatus(ShopStatus.CLOSED);
    }

    /***
     * Shop 상태에 따라, 다건조회
     */
    public List<ShopResponseDto> getOpenShops(int page, int size) {
        // Pageable 객체 생성 (페이지와 사이즈를 전달)
        Pageable pageable = PageRequest.of(page, size);

        // 상태가 OPEN인 가게들만 페이지네이션하여 조회
        Page<Shop> openShopsPage = shopRepository.findAllByShopStatus(ShopStatus.OPEN, pageable);

        // Shop 엔티티를 ShopResponseDto로 변환하여 반환
        return openShopsPage.map(ShopResponseDto::new).stream().toList();
    }



    /***
     * Shop 단건조회
     */
    public ShopResponseDto getShop(Long shopId) {
        Shop shop = shopRepository.findByShopId(shopId).orElseThrow(NoSignedUserException::new);
        return new ShopResponseDto(shop);
    }

    // 사장님의 영업 중인(OPEN) 가게 수만 카운트하는 메서드
    public int getActiveShopCount(Long ownerId) {
        // 상태가 OPEN인 가게만 카운트
        return shopRepository.countByOwnerIdAndShopStatus(ownerId, ShopStatus.OPEN);
    }
}
