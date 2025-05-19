package com.example.scheduler.repository;

import com.example.scheduler.entity.Comment;
import com.example.scheduler.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByScheduleOrderByCreatedAtDesc(Schedule schedule);
}
