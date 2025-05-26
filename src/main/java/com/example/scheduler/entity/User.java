package com.example.scheduler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보를 나타내는 JPA 엔티티 클래스입니다.
 * Timestamped를 상속받아 생성 및 수정 시간을 자동으로 관리합니다.
 * 'users' 테이블과 매핑됩니다.
 */

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {

    /**
     * 사용자의 고유 식별자 (Primary Key).
     * 데이터베이스에서 자동으로 생성되는 IDENTITY 전략을 사용합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자명 (닉네임).
     * null 값을 허용하지 않습니다.
     */
    @Column(nullable = false)
    private String username;

    /**
     * 사용자 이메일 주소.
     * null 값을 허용하지 않으며, 데이터베이스 수준에서 고유해야 합니다.
     * 로그인 ID 등으로 사용될 수 있습니다.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * 사용자 비밀번호.
     * null 값을 허용하지 않습니다.
     * 데이터베이스에는 암호화된 형태로 저장됩니다.
     */
    @Column(nullable = false)
    private String password;

    /**
     * 새로운 User 엔티티를 생성합니다.
     *
     * @param username 사용자의 이름 (닉네임)
     * @param email    사용자의 이메일 주소
     * @param password 사용자의 암호화된 비밀번호
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * 사용자의 이름(닉네임)과 이메일 정보를 수정합니다.
     * 비밀번호는 이 메소드를 통해 변경되지 않습니다.
     *
     * @param username 새 사용자명
     * @param email    새 이메일 주소
     */
    public void update(String username, String email) {
        this.username = username;
        this.email = email;
    }

    /**
     * 사용자의 비밀번호를 새로운 암호화된 비밀번호로 수정합니다.
     *
     * @param encodedPassword 새로 암호화된 비밀번호
     */
    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
