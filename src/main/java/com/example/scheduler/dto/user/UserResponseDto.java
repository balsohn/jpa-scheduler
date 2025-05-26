package com.example.scheduler.dto.user;

import com.example.scheduler.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 사용자 정보 조회 응답 시 사용되는 데이터 전송 객체(DTO)입니다.
 * {@link User} 엔티티의 정보를 클라이언트에게 전달합니다. (비밀번호 제외)
 */
@Getter
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    /**
     * {@link User} 엔티티를 기반으로 {@link UserResponseDto}를 생성합니다.
     *
     * @param user 응답으로 변환할 {@link User} 엔티티
     */
    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
