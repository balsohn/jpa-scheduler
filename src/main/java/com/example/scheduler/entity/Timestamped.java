package com.example.scheduler.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 엔티티의 생성 및 수정 시간을 자동으로 관리하기 위한 추상 클래스입니다.
 * 이 클래스를 상속하는 엔티티는 자동으로 생성 시간(createdAt)과
 * 마지막 수정 시간(modifiedAt) 필드를 갖게 됩니다.
 * JPA Auditing 기능을 사용하여 시간을 자동으로 채웁니다.
 *
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Timestamped {

    /**
     * 엔티티가 처음 생성된 시간입니다.
     * 데이터베이스에 저장될 때 자동으로 현재 시간이 기록됩니다.
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 엔티티가 마지막으로 수정된 시간입니다.
     * 데이터베이스에서 해당 레코드가 업데이트될 때마다 자동으로 현재 시간이 기록됩니다.
     */
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
