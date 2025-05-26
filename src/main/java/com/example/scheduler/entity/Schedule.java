package com.example.scheduler.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 스케줄 정보를 나타내는 JPA 엔티티 클래스입니다.
 *
 * 사용자가 작성할 일정 정보를 저장하며, {@link User}와 다대일 관계를, {@link Comment}와 일대다 관계를 가집니다.
 * {@link Timestamped}를 상속받아 생성일과 수정일은 자동으로 관리됩니다.
 * 'schedules' 테이블과 매핑됩니다.
 *
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "schedules")
public class Schedule extends Timestamped {

    /**
     * 스케줄의 고유 식별자 (Primary Key).
     * 데이터베이스에서 자동으로 생성되는 IDENTITY 전략을 사용합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 스케줄의 제목입니다.
     * null 값을 허용하지 않습니다.
     */
    @Column(nullable = false)
    private String title;

    /**
     * 스케줄의 상세 내용입니다.
     * null 값을 허용하지 않습니다.
     */
    @Column(nullable = false)
    private String content;

    /**
     * 스케줄을 작성한 사용자 (필수값)
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 이 스케줄에 달린 댓글 목록
     * 스케줄 삭제 시 연관된 댓글도 함께 삭제됩니다.
     */
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    /**
     * Schedule 엔티티의 생성자입니다.
     *
     * @param title 스케줄 제목
     * @param content 스케줄 내용
     * @param user 작성자 정보 {@link User}
     */
    public Schedule(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    /**
     * 스케줄 정보를 수정합니다.
     *
     * @param title 새로운 제목
     * @param content 새로운 내용
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }


    /**
     * 스케줄 작성자의 사용자명을 반환합니다.
     * @return 작성자의 사용자명
     */
    public String getUsername() {
        return this.user.getUsername();
    }
}
