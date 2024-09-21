package com.sparta.orderapp.repository;


import com.sparta.orderapp.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;




public interface ReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findAllByShop_ShopIdAndStarScoreBetween(Long id, int min, int max, Pageable pageable);

}
