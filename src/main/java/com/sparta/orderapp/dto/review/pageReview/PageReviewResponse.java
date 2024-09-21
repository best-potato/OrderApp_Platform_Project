package com.sparta.orderapp.dto.review.pageReview;

import com.sparta.orderapp.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PageReviewResponse {
    private Long reviewId;
    private String content;
    private int starScore;
    private Long userId;
    private Long shopId;
    private LocalDateTime createdAt;

    public PageReviewResponse(Review review){
        this.reviewId = review.getReviewId();
        this.content = review.getContent();
        this.starScore = review.getStarScore();
        this.createdAt = review.getCreatedAt();
        this.userId=review.getUser().getId();
        this.shopId=review.getShop().getShopId();

    }

}
