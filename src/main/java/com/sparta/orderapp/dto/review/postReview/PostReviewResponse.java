package com.sparta.orderapp.dto.review.postReview;

import com.sparta.orderapp.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostReviewResponse {

    private Long reviewId;
    private String content;
    private int starScore;
    private LocalDateTime createdAt; // createdAt 만든 시각.
    private Long shopId;
    private Long userId;

    public PostReviewResponse(Review review){
        this.reviewId = review.getReviewId();
        this.content = review.getContent();
        this.starScore = review.getStarScore();
        this.createdAt = review.getCreatedAt();
        this.shopId= review.getShop().getShopId();
        this.userId = review.getUser().getId();
    }
}
