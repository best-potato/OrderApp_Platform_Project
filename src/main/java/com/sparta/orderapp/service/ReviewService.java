package com.sparta.orderapp.service;

import com.sparta.orderapp.annotation.Auth;
import com.sparta.orderapp.dto.review.postReview.PostReviewRequest;
import com.sparta.orderapp.dto.review.postReview.PostReviewResponse;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.entity.Review;
import com.sparta.orderapp.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public PostReviewResponse postReview(PostReviewRequest reqDto, AuthUser authUser){
        Review review = new Review(reqDto,authUser);
        Review postReview = reviewRepository.save(review);
        return new PostReviewResponse(postReview);
    }


    public void test(){
//        reviewRepository.findAllBy

    }
}
