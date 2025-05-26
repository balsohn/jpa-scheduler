package com.example.scheduler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 정보를 나타내는 JPA 엔티티 클래스입니다.
 * {@link Timestamped}를 상속받아 생성 및 수정 시간을 자동으로 관리합니다.
 * 'comments' 테이블과 매핑됩니다.
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends Timestamped {

    /**
     * 댓글의 고유 식별자 (Primary Key).
     * 데이터베이스에서 자동으로 생성되는 IDENTITY 전략을 사용합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 댓글의 내용입니다.
     * null 값을 허용하지 않습니다.
     */
    @Column(nullable = false)
    private String content;

    /**
     * 댓글을 작성한 사용자입니다.
     * {@link User} 엔티티와 다대일(Many-to-One) 관계를 가집니다.
     * 'user_id' 컬럼을 통해 조인되며, null 값을 허용하지 않습니다.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 댓글이 달린 스케줄입니다.
     * {@link Schedule} 엔티티와 다대일(Many-to-One) 관계를 가집니다.
     * 'schedule_id' 컬럼을 통해 조인되며, null 값을 허용하지 않습니다.
     */
    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    /**
     * 새로운 Comment 엔티티를 생성합니다.
     *
     * @param content  댓글의 내용
     * @param user     댓글을 작성한 {@link User}
     * @param schedule 댓글이 달린 {@link Schedule}
     */
    public Comment(String content, User user, Schedule schedule) {
        this.content = content;
        this.user = user;
        this.schedule = schedule;
    }

    /**
     * 댓글의 내용을 수정합니다.
     *
     * @param content 새로운 댓글 내용
     */
    public void update(String content) {
        this.content = content;
    }

    /**
     * 댓글을 작성한 사용자의 이름을 반환합니다.
     * 이 메소드는 {@link User} 객체에 직접 접근하여 사용자 이름을 가져옵니다.
     *
     * @return 댓글 작성자의 사용자명 ({@link User#getUsername()})
     */
    public String getUsername() {
        return this.user.getUsername();
    }
}
