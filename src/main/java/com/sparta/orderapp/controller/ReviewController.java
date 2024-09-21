package com.sparta.orderapp.controller;

import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.dto.review.postReview.PostReviewRequest;
import com.sparta.orderapp.dto.review.postReview.PostReviewResponse;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{shopId}/reviews")
    public void postReview(@PathVariable Long shopId, @RequestBody PostReviewRequest reqDto, @Auth AuthUser authUser){
        reqDto.setShopId(shopId);
        PostReviewResponse resDto = reviewService.postReview(reqDto, authUser);
        ResponseEntity.ok().body(resDto);
    }
}
