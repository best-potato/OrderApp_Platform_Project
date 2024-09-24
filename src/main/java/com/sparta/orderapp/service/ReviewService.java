package com.sparta.orderapp.service;

import com.sparta.orderapp.dto.review.pageReview.PageReviewResponse;
import com.sparta.orderapp.dto.review.postReview.PostReviewRequest;
import com.sparta.orderapp.dto.review.postReview.PostReviewResponse;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.entity.Review;
import com.sparta.orderapp.exception.BadRequestException;
import com.sparta.orderapp.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * 리뷰를 작성하는 메서드
     * @param reqDto 리뷰 정보가 담긴 Dto객체
     * @param authUser 현재 로그인 중인 유저 정보
     * @return 작성된 리뷰에 대한 Dto
     */
    public PostReviewResponse postReview(PostReviewRequest reqDto, AuthUser authUser){
        if(reqDto.getContent()==null || reqDto.getShopId()==null){
            throw new BadRequestException("내용 또는 가게 id를 입력하세요.");
        }
        if(reqDto.getStarScore()>5 || reqDto.getStarScore()<0){
            throw new BadRequestException("별점은 0~5점 사이로 주세요.");
        }
        Review review = new Review(reqDto,authUser);
        Review postReview = reviewRepository.save(review);
        return new PostReviewResponse(postReview);
    }

    /**
     * min ~ max사이의 별점 리뷰를 페이징하여 보여주는 메서드
     * @param page 보여줄 페이지
     * @param size 한 페이지당 사이즈
     * @param min 최소 별점
     * @param max 최대 별점
     * @param shopId 리뷰 조회할 상점 Id
     * @return min ~ max별점 사이의 페이징된 리뷰
     */
    public Page<PageReviewResponse> pageReview(int page, int size, int min, int max, Long shopId){
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Review> reviews = reviewRepository.findAllByShop_ShopIdAndStarScoreBetween(shopId, min, max, pageable);
        return reviews.map(PageReviewResponse::new);
    }

}
