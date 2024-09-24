package com.sparta.orderapp.service;

import com.sparta.orderapp.dto.popular.PopularShopResponse;
import com.sparta.orderapp.entity.Shop;
import com.sparta.orderapp.repository.PopularShopRepository;
import com.sparta.orderapp.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PopularService {

    private final PopularShopRepository popularShopRepository;
    private final ShopRepository shopRepository;

    public List<PopularShopResponse> popularShop(){
        List<Long> shopsId= popularShopRepository.popularShopsRank();
        List<Shop> shops = shopRepository.findAllById(shopsId);

        // 가게 id와 이름만
        List<PopularShopResponse> list = shops.stream()
                .map(shop -> new PopularShopResponse(shop.getShopId(), shop.getShopName()))
                .toList();
        return list;


    }
}
