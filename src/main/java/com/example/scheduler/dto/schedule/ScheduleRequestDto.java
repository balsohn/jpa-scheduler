package com.example.scheduler.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 스케줄 생성 및 수정 요청 시 사용되는 데이터 전송 객체(DTO)입니다.
 * 클라이언트로부터 받은 데이터의 유효성 검증을 포함합니다.
 */
@Getter
@NoArgsConstructor
public class ScheduleRequestDto {

    /**
     * 스케줄의 제목입니다.
     * 필수 입력값이며, 최대 100자까지 입력 가능합니다.
     */
    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
    private String title;

    /**
     * 스케줄의 상세 내용입니다.
     * 필수 입력값입니다.
     */
    @NotBlank(message = "내용은 필수 입력값입니다.")
    private String content;
}
