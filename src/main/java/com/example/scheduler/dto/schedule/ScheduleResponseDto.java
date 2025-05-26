package com.example.scheduler.dto.schedule;

import com.example.scheduler.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 스케줄 단건 조회 또는 생성/수정 후 응답 시 사용되는 데이터 전송 객체(DTO)입니다.
 * {@link Schedule} 엔티티의 주요 정보를 클라이언트에게 전달합니다.
 */
@Getter
public class ScheduleResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    /**
     * {@link Schedule} 엔티티를 기반으로 {@link ScheduleResponseDto}를 생성합니다.
     *
     * @param schedule 응답으로 변환할 {@link Schedule} 엔티티
     */
    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.username = schedule.getUsername();
        this.createdAt = schedule.getCreatedAt();
        this.modifiedAt = schedule.getModifiedAt();
    }
}
