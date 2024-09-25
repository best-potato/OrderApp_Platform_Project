package com.sparta.orderapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {

    @CreatedDate
    @Column(updatable = false) // 업데이트 안되게 만들어야 함.
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt; // createdAt 만든 시각.

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP) // 데이터 타입 맞추기
    private LocalDateTime modifiedAt; // 조회한 entity 값이 변경 될 때마다, 해당 변경시간으로 변경됨.
}
