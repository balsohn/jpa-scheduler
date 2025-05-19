package com.example.scheduler.dto.schedule;

import com.example.scheduler.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SchedulePageResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private Long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public SchedulePageResponseDto(Schedule schedule, Long commnetCount) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.username = schedule.getUsername();
        this.commentCount = commnetCount;
        this.createdAt = schedule.getCreatedAt();
        this.modifiedAt = schedule.getModifiedAt();
    }
}
