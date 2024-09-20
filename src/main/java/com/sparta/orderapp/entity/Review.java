package com.sparta.orderapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "review")
public class Review{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String content;
    private int starScore;
    private Boolean status;

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


}
