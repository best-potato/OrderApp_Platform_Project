package com.sparta.orderapp.repository;

import com.sparta.orderapp.dto.ordersCache.PopularShop;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@RequiredArgsConstructor
@Repository
public class PopularShopRepository {
    private final String KEY = "PopularShop";
    private final RedisTemplate<String, Object> redisTemplate;

    // Sorted Set에 주문 저장
    public void saveOrder(Long shopId) {
        redisTemplate.opsForZSet().add(KEY, shopId, 1);

    }

    // 증가
    public void incrementScore(PopularShop popularShop){
        redisTemplate.opsForZSet().incrementScore(KEY, popularShop.getShopId(),1);
    }

    // 상위 10개의 주문 조회 (내림차순)
    public Set<Object> getTop10Orders() {
        return redisTemplate.opsForZSet().reverseRange(KEY, 0, 9);
    }
}
