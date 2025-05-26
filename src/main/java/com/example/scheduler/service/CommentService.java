package com.example.scheduler.service;

import com.example.scheduler.dto.comment.CommentRequestDto;
import com.example.scheduler.dto.comment.CommentResponseDto;
import com.example.scheduler.entity.Comment;
import com.example.scheduler.entity.Schedule;
import com.example.scheduler.entity.User;
import com.example.scheduler.repository.CommentRepository;
import com.example.scheduler.repository.ScheduleRepository;
import com.example.scheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 댓글 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 댓글 생성, 조회, 수정, 삭제 기능을 담당합니다.
 */
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    /**
     * 특정 스케줄에 새로운 댓글을 생성합니다.
     *
     * @param scheduleId 댓글을 추가할 스케줄의 ID ({@link Schedule#getId()})
     * @param requestDto 댓글 생성에 필요한 정보를 담은 {@link CommentRequestDto}
     * @param username   댓글을 작성하는 사용자의 이름 ({@link User#getUsername()}). 세션 등에서 추출된 값입니다.
     * @return 생성된 댓글의 정보를 담은 {@link CommentResponseDto}
     * @throws IllegalArgumentException 사용자를 찾을 수 없거나, 스케줄을 찾을 수 없거나, username이 null인 경우 발생 가능.
     */
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

    /**
     * 특정 스케줄에 달린 모든 댓글 목록을 조회합니다.
     * 댓글은 생성된 시간의 내림차순으로 정렬됩니다.
     *
     * @param scheduleId 댓글 목록을 조회할 스케줄의 ID ({@link Schedule#getId()})
     * @return 해당 스케줄의 댓글 목록 ({@link List}<{@link CommentResponseDto}>). 댓글이 없으면 빈 리스트가 반환될 수 있습니다.
     * @throws IllegalArgumentException 해당 ID의 스케줄을 찾을 수 없을 경우 발생
     */
    public List<CommentResponseDto> getComments(Long scheduleId) {
        // 일정 조회
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다. " + scheduleId));

        // 댓글 목록 조회 및 DTO 변환
        return commentRepository.findAllByScheduleOrderByCreatedAtDesc(schedule).stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 댓글의 내용을 수정합니다.
     * 이 작업은 트랜잭션 내에서 수행됩니다.
     * 댓글이 요청된 스케줄에 속하는지, 그리고 요청한 사용자가 댓글 작성자인지 확인합니다.
     *
     * @param scheduleId 댓글이 속한 스케줄의 ID ({@link Schedule#getId()})
     * @param commentId  수정할 댓글의 ID ({@link Comment#getId()})
     * @param requestDto 댓글 수정에 필요한 내용을 담은 {@link CommentRequestDto}
     * @param username   댓글 수정을 요청한 사용자의 이름 ({@link User#getUsername()}).
     * @return 수정된 댓글의 정보를 담은 {@link CommentResponseDto}
     * @throws IllegalArgumentException 댓글을 찾을 수 없거나, 스케줄 ID가 일치하지 않거나, 수정 권한이 없거나, username이 null인 경우 발생 가능.
     */
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

    /**
     * 특정 댓글을 삭제합니다.
     * 댓글이 요청된 스케줄에 속하는지, 그리고 요청한 사용자가 댓글 작성자인지 확인합니다.
     *
     * @param scheduleId 댓글이 속한 스케줄의 ID ({@link Schedule#getId()})
     * @param commentId  삭제할 댓글의 ID ({@link Comment#getId()})
     * @param username   댓글 삭제를 요청한 사용자의 이름 ({@link User#getUsername()}).
     * @throws IllegalArgumentException 댓글을 찾을 수 없거나, 스케줄 ID가 일치하지 않거나, 삭제 권한이 없거나, username이 null인 경우 발생 가능.
     */
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
