package com.sparta.orderapp.controller;

import com.sparta.orderapp.service.RedisGpsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    private final RedisGpsService redisGpsService;

    // 가게와 손님 위치 추가 Api
    @PostMapping("/addStoreAndCustomer")
    public String addStoreAndCustomer(@RequestParam String storeId, @RequestParam double storeLat, @RequestParam double storeLon,
                                      @RequestParam String customerId, @RequestParam double customerLat, @RequestParam double customerLon) {
        redisGpsService.addStoreAndCustomer(storeId, storeLat, storeLon, customerId, customerLat, customerLon);
        return "Store and customer locations added successfully.";
    }

    // 배달원 위치 업데이트 API
    @PostMapping("/updateLocation")
    public String updateDeliveryLocation(@RequestParam String deliveryId, @RequestParam double lat, @RequestParam double lon) {
        redisGpsService.updateDeliveryLocation(deliveryId, lat, lon);
        return "Delivery location updated successfully.";
    }

    // 배달원과 손님의 거리 조회 API
    @GetMapping("/distanceToCustomer")
    public Double getDistanceToCustomer(@RequestParam String deliveryId, @RequestParam String customerId) {
        return redisGpsService.getDistanceToCustomer(deliveryId, customerId);
    }

    // 배달 완료 후 배달원 위치 삭제 API
    @DeleteMapping("/completeDelivery")
    public String completeDelivery(@RequestParam String deliveryId) {
        redisGpsService.completeDelivery(deliveryId);
        return "Delivery completed and data removed successfully.";
    }
}
