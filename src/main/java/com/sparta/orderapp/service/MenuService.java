package com.sparta.orderapp.service;

import com.sparta.orderapp.dto.menu.MenuRequestDto;
import com.sparta.orderapp.dto.menu.CreateMenuResponseDto;
import com.sparta.orderapp.dto.menu.UpdateMenuResponseDto;
import com.sparta.orderapp.entity.Menu;
import com.sparta.orderapp.entity.Shop;
import com.sparta.orderapp.exception.BadRequestException;
import com.sparta.orderapp.exception.MethodNotAllowed;
import com.sparta.orderapp.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    // 메뉴 생성
    @Transactional
    public CreateMenuResponseDto createMenu(Long userId, Long shopId, MenuRequestDto requestDto) {
        //해당 유저아이디의 가게를 찾을 수 없을 경우
        // Shop shop = shopRepository.findByUserId(userId)
        //  .orElseThrow(() -> new IllegalArgumentException("해당 ID의 가게를 찾을 수 없습니다" + userId))
        Shop shop = new Shop();
        shop.setShopId(shopId);

        // 메뉴 이름이나 가격이 널값일 경우
        if (requestDto.getName() == null || requestDto.getPrice() <= 0) {
            throw new BadRequestException("메뉴 정보를 입력해주세요.");
        }

        Menu menu = new Menu(requestDto, shop);

        menuRepository.save(menu);
        return new CreateMenuResponseDto(menu);
    }

    // 메뉴 수정
    @Transactional
    public UpdateMenuResponseDto updateMenu(Long userId, Long shopId, Long menuId, MenuRequestDto requestDto) {
        // 해당 유저아이디의 가게가 아닌 경우
        // Shop shop = shopRepository.findByUserId(userId)
        //  .orElseThrow(() -> new IllegalArgumentException("해당 유저ID의 가게가 아닙니다" + userId))
        Shop shop = new Shop(shopId);

        // 해당 메뉴가 이 가게의 메뉴가 아닐 경우
        Menu menu = menuRepository.findByShop(shop)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 메뉴를 찾을 수 없습니다" + shopId));

        // 이미 삭제된 메뉴일 경우
        if (menu.getStatus()){
            throw new MethodNotAllowed("이미 삭제된 메뉴입니다.");
        }

        menu.menuUpdate(requestDto.getName(), requestDto.getPrice());
        menuRepository.save(menu);
        return new UpdateMenuResponseDto(menu.getMenuId(), menu.getName(), menu.getPrice(), "메뉴가 수정되었습니다.");
    }

    // 메뉴 삭제
    @Transactional
    public void deleteMenu(Long userId, Long shopId, Long menuId) {
        // 해당 유저아이디의 가게가 아닌 경우
        // Shop shop = shopRepository.findByUserId(userId)
        //  .orElseThrow(() -> new IllegalArgumentException("해당 유저ID의 가게가 아닙니다" + userId))

        //해당 메뉴를 찾을 수 없을 경우
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 메뉴를 찾을 수 없습니다" + menuId));

        menu.setStatus(true);
        menuRepository.save(menu);
    }
}
