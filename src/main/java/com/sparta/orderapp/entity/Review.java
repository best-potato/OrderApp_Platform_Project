package com.sparta.orderapp.entity;

import com.sparta.orderapp.dto.review.postReview.PostReviewRequest;
import com.sparta.orderapp.dto.user.AuthUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "review")
@EntityListeners(AuditingEntityListener.class)
public class Review{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = true)
    private int starScore;

    @Column(nullable = false)
    private Boolean status = false;

    // 수정일이 필요 없는 엔티티여서, Timestamped 클래스를 상속받지 않고 새로 생성일을 만들었습니다.
    @CreatedDate
    @Column(updatable = false) // 업데이트 안되게 만들어야 함.
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt; // createdAt 만든 시각.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    public Review(PostReviewRequest reqDto, AuthUser authUser){
        this.content = reqDto.getContent();
        this.starScore = reqDto.getStarScore();
        this.user = new User(authUser.getId());
        this.shop = new Shop(reqDto.getShopId());
    }


}
