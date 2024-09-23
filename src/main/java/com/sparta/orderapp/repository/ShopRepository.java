package com.sparta.orderapp.repository;

import com.sparta.orderapp.entity.Shop;
import com.sparta.orderapp.entity.ShopStatus;
import com.sparta.orderapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByShopId(Long shopId);
    Page<Shop> findAllByShopId(Long shopId, Pageable pageable);
    Optional<Shop> findByOwnerAndShopId(User user, Long shopId);
    int countByOwnerId(Long ownerId);
    Optional<Shop> findByShopIdAndOwnerId(Long shopId, Long ownerId);
    // 상태가 OPEN인 가게들만 페이지네이션하여 조회
    Page<Shop> findAllByStatus(ShopStatus status, Pageable pageable);

}