package com.example.scheduler.dto.schedule;

import com.example.scheduler.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 스케줄 페이징 조회 응답 시 사용되는 데이터 전송 객체(DTO)입니다.
 * {@link Schedule} 엔티티의 정보와 함께 해당 스케줄의 댓글 수를 포함합니다.
 */
@Getter
public class SchedulePageResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private Long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    /**
     * {@link Schedule} 엔티티와 댓글 수를 기반으로 {@link SchedulePageResponseDto}를 생성합니다.
     *
     * @param schedule     응답으로 변환할 {@link Schedule} 엔티티
     * @param commentCount 해당 스케줄의 댓글 수
     */
    public SchedulePageResponseDto(Schedule schedule, Long commentCount) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.username = schedule.getUsername();
        this.commentCount = commentCount;
        this.createdAt = schedule.getCreatedAt();
        this.modifiedAt = schedule.getModifiedAt();
    }
}
