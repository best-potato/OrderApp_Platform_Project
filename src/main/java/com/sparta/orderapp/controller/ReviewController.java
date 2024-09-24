package com.sparta.orderapp.controller;

import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.dto.review.pageReview.PageReviewResponse;
import com.sparta.orderapp.dto.review.postReview.PostReviewRequest;
import com.sparta.orderapp.dto.review.postReview.PostReviewResponse;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰를 작성하는 메서드
     * @param shopId 리뷰를 작성할 상점 Id
     * @param reqDto 리뷰 작성에 필요한 정보가 담긴 RequestDto
     * @param authUser 현재 로그인중인 유저에 대한 정보
     */
    @PostMapping("/{shopId}/reviews")
    public void postReview(@PathVariable Long shopId, @RequestBody PostReviewRequest reqDto, @Auth AuthUser authUser){
        reqDto.setShopId(shopId);
        PostReviewResponse resDto = reviewService.postReview(reqDto, authUser);
        ResponseEntity.ok().body(resDto);
    }

    /**
     * min ~ max점 사이의 별점을 갖고 있는 리뷰를 페이징하여 조회하는 메서드
     * @param page 확인할 페이지
     * @param size 1 페이지당 사이즈
     * @param min 최소 별점
     * @param max 최대 별점
     * @param shopId 조회할 상점 Id
     * @return 리뷰 페이징 결과물
     */
    @GetMapping("/{shopId}/reviews")
    public ResponseEntity<Page<PageReviewResponse>> pageReview(@RequestParam int page,
                           @RequestParam int size,
                           @RequestParam int min,
                           @RequestParam int max,
                           @PathVariable Long shopId
                           ){
        Page<PageReviewResponse> pageDto = reviewService.pageReview(page, size, min, max, shopId);
        return ResponseEntity.ok().body(pageDto);
    }
}
