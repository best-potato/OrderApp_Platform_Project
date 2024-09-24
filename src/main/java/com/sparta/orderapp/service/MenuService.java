package com.sparta.orderapp.service;

import com.sparta.orderapp.dto.menu.CreateMenuResponseDto;
import com.sparta.orderapp.dto.menu.MenuRequestDto;
import com.sparta.orderapp.dto.menu.UpdateMenuResponseDto;
import com.sparta.orderapp.entity.Menu;
import com.sparta.orderapp.entity.Shop;
import com.sparta.orderapp.entity.User;
import com.sparta.orderapp.repository.MenuRepository;
import com.sparta.orderapp.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;

    /**
     * ShopId를 가진 가게에 메뉴를 추가하는 메서드
     * @param userId 가게 오너 Id
     * @param shopId 가게 Id
     * @param requestDto 추가할 메뉴에 대한 정보가 담긴 Dto
     * @return 새로 추가한 메뉴에 대한 정보가 담긴 Dto
     */
    @Transactional
    public CreateMenuResponseDto createMenu(Long userId, Long shopId, MenuRequestDto requestDto) {
        User user = new User(userId);
        Shop shop = shopRepository.findByOwnerAndShopId(user, shopId)
          .orElseThrow(() -> new IllegalArgumentException("해당 유저의 가게를 찾을 수 없습니다."));

        Menu menu = new Menu(requestDto, shop);
        menuRepository.save(menu);
        return new CreateMenuResponseDto(menu);
    }


    /**
     * menuId에 해당하는 menu에 대한 정보를 갱신하는 메서드
     * @param userId 가게 오너 Id
     * @param shopId 가게 Id
     * @param menuId 변경할 메뉴 Id
     * @param requestDto 해당 메뉴에 대해 변경할 내용
     * @return 갱신된 메뉴에 대한 정보가 담긴 Dto
     */
    @Transactional
    public UpdateMenuResponseDto updateMenu(Long userId, Long shopId, Long menuId, MenuRequestDto requestDto) {
        User user = new User(userId);
        shopRepository.findByOwnerAndShopId(user, shopId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 가게를 찾을 수 없습니다."));

        // 해당 메뉴가 이 가게의 메뉴가 아닐 경우
       Menu menu = menuRepository.findByShop_ShopIdAndMenuId(shopId, menuId)
               .orElseThrow(() -> new IllegalArgumentException("해당 메뉴는 이 가게의 메뉴가 아닙니다."));

        menu.menuUpdate(requestDto.getName(), requestDto.getPrice());
        menuRepository.save(menu);
        return new UpdateMenuResponseDto(menu.getMenuId(), menu.getName(), menu.getPrice(), "메뉴가 수정되었습니다.");
    }


    /**
     * 해당 menu를 삭제하는 메서드
     * @param userId 가게 오너 Id
     * @param shopId 해당 가게 Id
     * @param menuId 삭제할 메뉴 Id
     */
    @Transactional
    public void deleteMenu(Long userId, Long shopId, Long menuId) {
        User user = new User(userId);
        shopRepository.findByOwnerAndShopId(user, shopId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 가게를 찾을 수 없습니다."));

        //해당 메뉴를 찾을 수 없을 경우
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 메뉴를 찾을 수 없습니다."));

        //이미 삭제한 메뉴일 경우
        if(menu.getStatus()) {
            throw new IllegalArgumentException("이미 삭제한 메뉴입니다.");
        }

        menu.setStatus(true);
        menuRepository.save(menu);
    }
}
