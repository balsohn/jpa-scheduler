package com.example.scheduler.service;

import com.example.scheduler.dto.comment.CommentRequestDto;
import com.example.scheduler.dto.comment.CommentResponseDto;
import com.example.scheduler.entity.Comment;
import com.example.scheduler.entity.Schedule;
import com.example.scheduler.entity.User;
import com.example.scheduler.repository.CommentRepository;
import com.example.scheduler.repository.ScheduleRepository;
import com.example.scheduler.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, ScheduleRepository scheduleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // 댓글 생성
    public CommentResponseDto createComment(
            Long scheduleId,
            CommentRequestDto requestDto,
            String username) {

        // 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. " + username));

        // 일정 조회
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다. " + scheduleId));

        // 댓글 생성
        Comment comment = new Comment(requestDto.getContent(), user, schedule);
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment);
    }

    // 댓글 조회
    public List<CommentResponseDto> getComments(Long scheduleId) {
        // 일정 조회
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다. " + scheduleId));

        // 댓글 목록 조회 및 DTO 변환
        return commentRepository.findAllByScheduleOrderByCreatedAtDesc(schedule).stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long scheduleId, Long commentId, CommentRequestDto requestDto, String username) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. " + commentId));

        // 댓글이 해당 일정에 속하는지 확인
        if (!comment.getSchedule().getId().equals(scheduleId)) {
            throw new IllegalArgumentException("해당 일정에 속한 댓글이 아닙니다.");
        }

        // 작성자 확인
        if (!comment.getUsername().equals(username)) {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
        }

        // 댓글 내용 수정
        comment.update(requestDto.getContent());

        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long scheduleId, Long commentId, String username) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. " + commentId));

        // 댓글이 해당 일정에 속하는지 확인
        if (!comment.getSchedule().getId().equals(scheduleId)) {
            throw new IllegalArgumentException("해당 일정에 속한 댓글이 아닙니다.");
        }

        // 사용자 확인
        if (!comment.getUsername().equals(username)) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }

        // 댓글 삭제
        commentRepository.delete(comment);
    }
}
