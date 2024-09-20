package com.sparta.orderapp.controller;

import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.dto.menu.CreateMenuRequestDto;
import com.sparta.orderapp.dto.menu.CreateMenuResponseDto;
import com.sparta.orderapp.dto.menu.UpdateMenuRequestDto;
import com.sparta.orderapp.dto.menu.UpdateMenuResponseDto;
import com.sparta.orderapp.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    //메뉴 생성
    //    * 메뉴 생성, 수정은 사장님만 할 수 있습니다.
    //    * 사장님은 본인 가게에만 메뉴를 등록할 수 있습니다.
    @PostMapping("/{shopId}/menus")
    public ResponseEntity<CreateMenuResponseDto> createMenu ( @PathVariable Long shopId, @RequestBody CreateMenuRequestDto requestDto) {
        return ResponseEntity.ok(menuService.createMenu(shopId, requestDto));
    }

    // 메뉴 수정
    @PutMapping("/{shopId}/menus/{menuId}")
    public ResponseEntity<UpdateMenuResponseDto> updateMenu (@PathVariable Long shopId, @PathVariable Long menuId, @RequestBody UpdateMenuRequestDto requestDto) {
        return ResponseEntity.ok(menuService.updateMenu(shopId, menuId, requestDto));
    }

    //* 메뉴 삭제
    //    * 본인 가게의 메뉴만 삭제할 수 있습니다.
    //    * 삭제 시, 메뉴의 상태만 삭제 상태로 변경됩니다.
    //        * 가게 메뉴 조회 시 삭제된 메뉴는 나타나지 않습니다.
    //        * 주문 내역 조회 시에는 삭제된 메뉴의 정보도 나타납니다.


}
