package com.example.scheduler.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 생성 및 수정 요청 시 사용되는 데이터 전송 객체(DTO)입니다.
 */
@Getter
@NoArgsConstructor
public class CommentRequestDto {

    /**
     * 댓글의 내용입니다.
     * 필수 입력값이며, 비어 있을 수 없습니다.
     */
    @NotBlank(message = "댓글 내용은 필수 입력값입니다.")
    private String content;
}
