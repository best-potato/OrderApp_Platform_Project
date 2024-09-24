package com.sparta.orderapp.controller;

import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.dto.menu.*;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owners/shops")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    //메뉴 생성
    @PostMapping("/{shopId}/menus")
    public ResponseEntity<CreateMenuResponseDto> createMenu (
            @Auth AuthUser authUser,
            @PathVariable Long shopId,
            @RequestBody MenuRequestDto requestDto) {
        return ResponseEntity.ok(menuService.createMenu(authUser.getId(), shopId, requestDto));
    }

    // 메뉴 수정
    @PutMapping("/{shopId}/menus/{menuId}")
    public ResponseEntity<UpdateMenuResponseDto> updateMenu (
            @Auth AuthUser authUser,
            @PathVariable Long shopId,
            @PathVariable Long menuId,
            @RequestBody MenuRequestDto requestDto) {
        UpdateMenuResponseDto responseDto = menuService.updateMenu(authUser.getId(),shopId, menuId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    // 메뉴 삭제
    @DeleteMapping("/{shopId}/menus/{menuId}")
    public ResponseEntity<DeleteMenuResponseDto> deleteMenu(
            @Auth AuthUser authUser,
            @PathVariable Long shopId,
            @PathVariable Long menuId) {
        menuService.deleteMenu(authUser.getId(), shopId,menuId);
        return ResponseEntity.ok(new DeleteMenuResponseDto("해당 메뉴가 삭제 되었습니다."));
    }

}
