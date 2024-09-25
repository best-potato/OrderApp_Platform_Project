package com.sparta.orderapp.controller;

import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.dto.menu.CreateMenuResponseDto;
import com.sparta.orderapp.dto.menu.DeleteMenuResponseDto;
import com.sparta.orderapp.dto.menu.MenuRequestDto;
import com.sparta.orderapp.dto.menu.UpdateMenuResponseDto;
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

    /**
     * 메뉴를 추가하는 메서드
     * @param authUser 현재 로그인중인 유저에 대한 정보
     * @param shopId 메뉴를 추가할 상점 Id
     * @param requestDto 메뉴 추가에 필요한 정보가 담긴 Request Dto
     * @return 200 : 메뉴 추가 성공
     */
    @PostMapping("/{shopId}/menus")
    public ResponseEntity<CreateMenuResponseDto> createMenu (
            @Auth AuthUser authUser,
            @PathVariable Long shopId,
            @RequestBody MenuRequestDto requestDto) {
        return ResponseEntity.ok(menuService.createMenu(authUser.getId(), shopId, requestDto));
    }

    /**
     * 메뉴 정보를 갱신하는 메서드
     * @param authUser 현재 로그인중인 유저에 대한 정보
     * @param shopId 메뉴를 추가할 상점 Id
     * @param menuId 갱신할 메뉴 Id
     * @param requestDto 메뉴 업데이트에 필요한 정보가 담긴 Request Dto
     * @return 200 : 메뉴 정보 변경 성공
     */
    @PutMapping("/{shopId}/menus/{menuId}")
    public ResponseEntity<UpdateMenuResponseDto> updateMenu (
            @Auth AuthUser authUser,
            @PathVariable Long shopId,
            @PathVariable Long menuId,
            @RequestBody MenuRequestDto requestDto) {
        UpdateMenuResponseDto responseDto = menuService.updateMenu(authUser.getId(),shopId, menuId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    /**
     * 메뉴를 삭제하는 메서드
     * @param authUser 현재 로그인 중인 유저에 대한 정보
     * @param shopId 메뉴를 삭제할 상점 Id
     * @param menuId 삭제할 메뉴 Id
     * @return 200 : 메뉴 삭제 성공
     */
    @DeleteMapping("/{shopId}/menus/{menuId}")
    public ResponseEntity<DeleteMenuResponseDto> deleteMenu(
            @Auth AuthUser authUser,
            @PathVariable Long shopId,
            @PathVariable Long menuId) {
        menuService.deleteMenu(authUser.getId(), shopId,menuId);
        return ResponseEntity.ok(new DeleteMenuResponseDto("해당 메뉴가 삭제 되었습니다."));
    }

}
