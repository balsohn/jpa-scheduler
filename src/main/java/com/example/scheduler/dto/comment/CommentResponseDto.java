package com.example.scheduler.dto.comment;

import com.example.scheduler.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 댓글 조회 응답 시 사용되는 데이터 전송 객체(DTO)입니다.
 * {@link Comment} 엔티티의 정보를 클라이언트에게 전달하는 데 사용됩니다.
 */
@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    /**
     * {@link Comment} 엔티티를 기반으로 {@link CommentResponseDto}를 생성합니다.
     *
     * @param comment 응답으로 변환할 {@link Comment} 엔티티
     */
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = comment.getUsername();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
