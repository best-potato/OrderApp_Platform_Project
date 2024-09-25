package com.sparta.orderapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RedisGpsService {
    private final RedisTemplate<String, String> redisTemplate;

    private static final String LOCATION_KEY = "locations";


    // 가게와 손님의 위치 추가
    public void addStoreAndCustomer(String storeId, double storeLat, double storeLon, String customerId, double customerLat, double customerLon) {
        redisTemplate.opsForGeo().add(LOCATION_KEY, new RedisGeoCommands.GeoLocation<>(storeId, new org.springframework.data.geo.Point(storeLon, storeLat)));
        redisTemplate.opsForGeo().add(LOCATION_KEY, new RedisGeoCommands.GeoLocation<>(customerId, new org.springframework.data.geo.Point(customerLon, customerLat)));
    }

    // 배달원의 위치 업데이트
    public void updateDeliveryLocation(String deliveryId, double lat, double lon) {
        redisTemplate.opsForGeo().add(LOCATION_KEY, new RedisGeoCommands.GeoLocation<>(deliveryId, new org.springframework.data.geo.Point(lon, lat)));
    }

    // 배달원과 손님의 거리 계산
    public Double getDistanceToCustomer(String deliveryId, String customerId) {
        return Objects.requireNonNull(redisTemplate.opsForGeo().distance(LOCATION_KEY, deliveryId, customerId, RedisGeoCommands.DistanceUnit.METERS)).getValue();
    }

    // 배달 완료 시 배달원 위치 삭제
    public void completeDelivery(String deliveryId) {
        redisTemplate.opsForZSet().remove(LOCATION_KEY, deliveryId);
    }

}
