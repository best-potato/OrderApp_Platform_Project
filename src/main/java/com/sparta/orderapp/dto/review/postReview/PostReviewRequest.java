package com.sparta.orderapp.dto.review.postReview;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostReviewRequest {
    Long shopId;
    String content;
    int starScore;
}
