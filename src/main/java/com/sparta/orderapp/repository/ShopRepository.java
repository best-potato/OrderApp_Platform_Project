package com.sparta.orderapp.repository;

import com.sparta.orderapp.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByShopId(Long shopId);
    Page<Shop> findAllByShopId(Long shopId, Pageable pageable);
}