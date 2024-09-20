package com.sparta.orderapp.repository;

import com.sparta.orderapp.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
}
