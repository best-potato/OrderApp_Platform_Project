package com.sparta.orderapp.repository;

import com.sparta.orderapp.dto.ordersCache.PopularShop;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class PopularShopRepository {
    private final String date = LocalDate.now().toString();
    private final String KEY = "popularShop:"+date;
    private final RedisTemplate<String, Object> redisTemplate;

    public void orderCompleteShop(Long shopId) {
        redisTemplate.opsForList().rightPush(KEY, shopId);
        redisTemplate.expire(KEY, 1, TimeUnit.DAYS);
    }

    // 인기 가게 랭킹
    public List<Long> popularShopsRank() {

        // Redis에서 모든 데이터를 가져오기
        List<Object> userIdList = redisTemplate.opsForList().range(KEY, 0, -1);

        if (userIdList == null) {
            return Collections.emptyList();
        }

        // ID 카운트하기
        Map<Long, Long> countMap = userIdList.stream()
                .map(id -> Long.valueOf(id.toString())) // Object를 Long으로 변환
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // 카운트 결과를 많은 순서대로 정렬하고 상위 10개의 키만 추출
        return countMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(10) // 상위 10개만 가져오기
                .map(Map.Entry::getKey) // 키만 추출
                .collect(Collectors.toList());
    }
    // 주간 판매왕 랭킹 검색
    // 이건 판매 수까지 나옴
    public Map<Long, Long> countAndSort() {

        String popularShop = "popularShop:" + date;

        // Redis에서 모든 데이터를 가져오기
        List<Object> userIdList = redisTemplate.opsForList().range(popularShop, 0, -1);

        if (userIdList == null) {
            return Collections.emptyMap();
        }

        // ID 카운트하기
        Map<Long, Long> countMap = userIdList.stream()
                .map(id -> Long.valueOf(id.toString())) // Object를 Long으로 변환
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // 카운트 결과를 많은 순서대로 정렬
        return countMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }


}
