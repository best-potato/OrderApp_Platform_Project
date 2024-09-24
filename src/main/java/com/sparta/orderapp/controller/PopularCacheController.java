package com.sparta.orderapp.controller;

import com.sparta.orderapp.dto.popular.PopularShopResponse;
import com.sparta.orderapp.repository.PopularShopRepository;
import com.sparta.orderapp.service.PopularService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/popular")
public class PopularCacheController {

    private final PopularService popularService;

    @GetMapping("popular-shop")
    public ResponseEntity<List<PopularShopResponse>> popularShop() {
        List<PopularShopResponse> result=popularService.popularShop();
        return ResponseEntity.ok(result);
    }

}
