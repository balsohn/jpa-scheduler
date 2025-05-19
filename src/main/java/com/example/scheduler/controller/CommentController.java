package com.example.scheduler.controller;

import com.example.scheduler.dto.comment.CommentRequestDto;
import com.example.scheduler.dto.comment.CommentResponseDto;
import com.example.scheduler.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedules/{scheduleId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long scheduleId,
            @Valid @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request
            ) {

        // 세션에서 사용자 이름 가져오기
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        CommentResponseDto responseDto = commentService.createComment(scheduleId, requestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 댓글조회
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long scheduleId) {
        List<CommentResponseDto> responseDtos = commentService.getComments(scheduleId);
        return ResponseEntity.ok(responseDtos);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request) {

        // 세션에서 사용자이름 가져오기
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        CommentResponseDto responseDto = commentService.updateComment(scheduleId, commentId, requestDto, username);
        return ResponseEntity.ok(responseDto);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, String>> deleteComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            HttpServletRequest request
    ) {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        commentService.deleteComment(scheduleId, commentId, username);
        return ResponseEntity.ok(Map.of("msg", "댓글이 삭제되었습니다."));
    }

}
