package com.sparta.orderapp.service;

import com.sparta.orderapp.dto.menu.CreateMenuRequestDto;
import com.sparta.orderapp.dto.menu.CreateMenuResponseDto;
import com.sparta.orderapp.dto.menu.UpdateMenuRequestDto;
import com.sparta.orderapp.dto.menu.UpdateMenuResponseDto;
import com.sparta.orderapp.entity.Menu;
import com.sparta.orderapp.entity.Shop;
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
    // 메뉴 생성, 수정은 사장님만 할 수 있습니다.
    // 사장님은 본인 가게에만 메뉴를 등록할 수 있습니다.
    // 예외처리
    @Transactional
    public CreateMenuResponseDto createMenu(Long shopId, CreateMenuRequestDto requestDto) {
        Shop shop = new Shop();
        shop.setShopId(shopId);

        Menu menu = new Menu(requestDto, shop);
        menuRepository.save(menu);

        return new CreateMenuResponseDto(menu);
    }

    // 메뉴 수정
    @Transactional
    public UpdateMenuResponseDto updateMenu(Long shopId, Long menuId, UpdateMenuRequestDto requestDto) {
        //해당 아이디의 샵을 찾을 수 없을 경우
        // Shop shop = shopRepository.findById(shopId)
        //  .orElseThrow(() -> new IllegalArgumentException("해당 ID의 가게를 찾을 수 없습니다" + shopId));
        Shop shop = new Shop(shopId);

        //해당 아이디의 메뉴를 찾을 수 없을 경우
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 메뉴를 찾을 수 없습니다" + menuId));

        // 메뉴 수정 권한이 없을 경우

        // 이미 삭제된 메뉴일 경우
        if (menu.getStatus()){
            throw new MethodNotAllowed("이미 삭제된 메뉴입니다.");
        }

        menu.menuUpdate(requestDto.getName(), requestDto.getPrice());
        menuRepository.save(menu);

        return new UpdateMenuResponseDto(menu.getMenuId(), menu.getName(), menu.getPrice(), "메뉴가 수정되었습니다.");

    }


}
