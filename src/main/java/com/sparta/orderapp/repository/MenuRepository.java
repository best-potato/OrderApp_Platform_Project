package com.sparta.orderapp.repository;

import com.sparta.orderapp.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByShop_ShopIdAndMenuId(Long shopId, Long menuId);

}
