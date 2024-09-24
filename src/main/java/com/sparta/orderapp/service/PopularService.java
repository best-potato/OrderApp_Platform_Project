package com.sparta.orderapp.service;

import com.sparta.orderapp.dto.popular.PopularShopResponse;
import com.sparta.orderapp.entity.Shop;
import com.sparta.orderapp.repository.PopularShopRepository;
import com.sparta.orderapp.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PopularService {

    private final PopularShopRepository popularShopRepository;
    private final ShopRepository shopRepository;

//    public List<PopularShopResponse> popularShop(){
////        public List<Long> popularShop() {
//            List<Long> shopsId = popularShopRepository.popularShopsRank();
//        List<Shop> shops = shopRepository.findAllById(shopsId);
//
//        // Shop 엔티티들을 Map으로 변환 (Key: Shop ID, Value: Shop)
//        Map<Long, Shop> shopMap = shops.stream()
//                .collect(Collectors.toMap(Shop::getId, shop -> shop));
//
//        // 원본 shopsId 리스트의 순서에 맞게 PopularShopResponse 리스트 생성
//        List<PopularShopResponse> list = shopsId.stream()
//                .map(shopMap::get)
//                .filter(Objects::nonNull) // Shop이 없는 ID는 제외
//                .map(shop -> new PopularShopResponse(shop.getShopId(), shop.getName()))
//                .collect(Collectors.toList());
//
//        return list;
////
//////        // 가게 id와 이름만
////        List<PopularShopResponse> list = shops.stream()
////                .map(shop -> new PopularShopResponse(shop.getShopId(), shop.getShopName()))
////                .toList();
////        return list;
////        //            return shopsId;
//
//
//        }
    public  Map<Long, Long> salesVolume(){
        return popularShopRepository.countAndSort();
    }

    public List<Long> popularShopSellAmount() {
        List<Long> shopsId = popularShopRepository.popularShopsRank();
        return shopsId;
    }
}
