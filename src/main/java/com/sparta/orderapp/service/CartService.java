package com.sparta.orderapp.service;

import com.sparta.orderapp.dto.cart.CartCache;
import com.sparta.orderapp.dto.user.AuthUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;

    public CartService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 장바구니 생성 + 같은 ShopId이면 리스트에 메뉴 추가됨
    public CartCache addItemToCart(AuthUser authUser, Long shopId, List<Long> menuIds) {
        try {
            String cartKey = "cart:" + authUser.getId();
            String shopKey = "cart_shop:" + authUser.getId();

            // 현재 장바구니의 가게아이디 가져오기 (String 타입으로 가져와서 Long으로 변환)
            Object o = redisTemplate.opsForValue().get(shopKey);
            String currentShopIdStr = o!=null ? String.valueOf(o) : null;

            Long currentShopId = currentShopIdStr != null ? Long.valueOf(currentShopIdStr) : null;

            if (currentShopId != null && !currentShopId.equals(shopId)) {
                // 가게 ID가 다르면 장바구니 초기화
                redisTemplate.delete(cartKey);
                redisTemplate.opsForValue().set(shopKey, shopId);
            } else if (currentShopId == null) {
                // 가게 ID가 처음 설정되는 경우
                redisTemplate.opsForValue().set(shopKey, shopId);
            }

            redisTemplate.opsForValue().set(shopKey, shopId);

            // 메뉴아이디 리스트로 저장
            for (Long menuId : menuIds) {
                redisTemplate.opsForList().rightPush(cartKey, menuId);
            }

            // 장바구니는 하루만 유지
            redisTemplate.expire(cartKey, 24, TimeUnit.HOURS);
            redisTemplate.expire(shopKey, 24, TimeUnit.HOURS);

            return new CartCache(authUser.getId(), shopId, menuIds);

        } catch (Exception e) {
            System.out.println("e = " + e);
            throw new RuntimeException();
        }

    }

    // 장바구니 조회
    public List<Object> getCartItems(AuthUser authUser) {
        String cartKey = "cart:" + authUser.getId();
        // 모든 메뉴 조회
        return redisTemplate.opsForList().range(cartKey, 0, -1);
    }

    // 장바구니 삭제
    public void clearCart(AuthUser authUser) {
        String cartKey = "cart:" + authUser.getId();
        redisTemplate.delete(cartKey);
    }


}
