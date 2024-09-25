package com.sparta.orderapp;

import com.sparta.orderapp.dto.review.postReview.PostReviewRequest;
import com.sparta.orderapp.dto.shop.ShopRequestDto;
import com.sparta.orderapp.dto.user.AuthUser;
import com.sparta.orderapp.entity.*;
import org.springframework.test.util.ReflectionTestUtils;

public class TestUtil {
    public static User getUser() {
        return new User("a@b.com", "password", "Names", "OWNER");
    }
    public static User getDisableUser() {
        User user = getUser();
        ReflectionTestUtils.setField(user, "user_status", UserStatusEnum.DISABLE);
        return user;
    }

    public static User getUser(long id) {
        User user = getUser();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    public static AuthUser authUser(long id) {
        return new AuthUser(id, "kw", UserRole.OWNER);
    }

    public static Shop getShop(long id) {
        ShopRequestDto shopRequestDto = new ShopRequestDto("a", "b", "c", 100);
        User user = getUser();
        Shop shop = new Shop(user, shopRequestDto);
        ReflectionTestUtils.setField(shop, "shopId", id);

        return shop;
    }

    public static Review getReview(PostReviewRequest request, long reviewId) {
        AuthUser authUser = authUser(1L);
        Review review = new Review(request,authUser);
        ReflectionTestUtils.setField(review, "reviewId", reviewId);
        return review;
    }
}
