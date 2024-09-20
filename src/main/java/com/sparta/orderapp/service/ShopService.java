package com.sparta.orderapp.service;

import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.dto.shop.ShopRequestDto;
import com.sparta.orderapp.dto.shop.ShopResponseDto;
import com.sparta.orderapp.dto.sign.SignupResponseDto;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.entity.Shop;
import com.sparta.orderapp.entity.User;
import com.sparta.orderapp.exception.NoSignedUserException;
import com.sparta.orderapp.exception.NotFoundException;
import com.sparta.orderapp.repository.ShopRepository;
import com.sparta.orderapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        Shop shop = findShop(shopId);
        commonService.confirmCreator(authUser.getId(), shopId, false);
        shop.update(requestDto);
        Shop saveComment = shopRepository.save(shop);
        return new ShopResponseDto(saveComment);
    }

    /***
     * Shop 다건조회
     */
    public Page<ShopResponseDto> getShops(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Shop> receiveList = shopRepository.findAll(pageable);
        return receiveList
                .map(ShopResponseDto::new);
    }


    /***
     * Shop 단건조회
     */
    public ShopResponseDto getShop(Long shopId) {
        Shop shop = shopRepository.findByShopId(shopId).orElseThrow(NoSignedUserException::new);
        return new ShopResponseDto(shop);
    }

    private Shop findShop (Long shopId) {
        return shopRepository.findById(shopId).orElseThrow(()->new NotFoundException("해당하는 아이디의 댓글이 존재하지 않습니다."));
    }
}
