package com.sparta.orderapp.service.reviewservice;

import com.sparta.orderapp.TestUtil;
import com.sparta.orderapp.dto.review.pageReview.PageReviewResponse;
import com.sparta.orderapp.dto.review.postReview.PostReviewRequest;
import com.sparta.orderapp.dto.review.postReview.PostReviewResponse;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.entity.Review;
import com.sparta.orderapp.repository.ReviewRepository;
import com.sparta.orderapp.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    public void 리뷰_작성에_성공한다() {
        long userId = 1L;
        long reviewId = 1L;
        PostReviewRequest postReviewRequest = new PostReviewRequest(1L, "b", 3);
        AuthUser authUser = TestUtil.authUser(userId);
        Review review = TestUtil.getReview(postReviewRequest, reviewId);

        given(reviewRepository.save(any())).willReturn(review);

        PostReviewResponse dto = reviewService.postReview(postReviewRequest, authUser);

        assertNotNull(dto);
    }

    @Test
    public void 리뷰_페이징에_성공한다() {
        long userId = 1L;
        int page = 1;
        int size = 10;
        int min = 1;
        int max = 5;
        long shopId = 1L;
        long reviewId = 1L;
        PostReviewRequest postReviewRequest = new PostReviewRequest(1L, "b", 3);
        AuthUser authUser = TestUtil.authUser(userId);
        Review review = TestUtil.getReview(postReviewRequest, reviewId);

        List<Review> reviews = new ArrayList<>(List.of(
                TestUtil.getReview(postReviewRequest, reviewId),
                TestUtil.getReview(postReviewRequest, reviewId),
                TestUtil.getReview(postReviewRequest, reviewId),
                TestUtil.getReview(postReviewRequest, reviewId)));

        Page<Review> reviewsPage = new PageImpl<>(reviews.stream().toList());
        given(reviewRepository.findAllByShop_ShopIdAndStarScoreBetween(any(), eq(min), eq(max), any())).willReturn(reviewsPage);
        Page<PageReviewResponse> dto = reviewService.pageReview(page,size,min,max,shopId);

        assertNotNull(dto);
    }
}
