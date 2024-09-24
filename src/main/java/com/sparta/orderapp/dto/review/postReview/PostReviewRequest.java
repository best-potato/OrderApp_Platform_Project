package com.sparta.orderapp.dto.review.postReview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PostReviewRequest {
    Long shopId;
    String content;
    int starScore;
}
