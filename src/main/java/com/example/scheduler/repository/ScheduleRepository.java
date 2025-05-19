package com.example.scheduler.repository;

import com.example.scheduler.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 수정일 기준 내림차순 정렬된 일정 조회(페이징)
    Page<Schedule> findAllByOrderByModifiedAtDesc(Pageable pageable);
}
