package com.example.scheduler.controller;

import com.example.scheduler.dto.comment.CommentRequestDto;
import com.example.scheduler.dto.comment.CommentResponseDto;
import com.example.scheduler.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 댓글 관리를 위한 REST API 컨트롤러입니다.
 *
 * 이 컨트롤러는 댓글의 생성, 특정 스케줄에 대한 댓글 목록 조회, 특정 댓글의 수정 및 삭제 기능을 제공합니다.
 * 댓글 수정 및 삭제는 해당 댓글을 작성한 사용자만 가능합니다.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/schedules/{scheduleId}/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 특정 스케줄에 새로운 댓글을 생성합니다.
     *
     * @param scheduleId 댓글을 추가할 대상 스케줄의 고유 ID.
     * @param requestDto 댓글 생성에 필요한 정보를 담은 DTO ({@link CommentRequestDto}). {@code @Valid} 어노테이션으로 유효성 검사가 수행됩니다.
     * @param request    HTTP 요청 객체. 세션에서 현재 로그인한 사용자의 username(댓글 작성자)을 추출하는 데 사용됩니다.
     * @return           생성된 댓글의 상세 정보를 담은 {@link CommentResponseDto}와 HTTP 201 Created 상태 코드를 포함하는 {@link ResponseEntity}.
     */
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long scheduleId,
            @Valid @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request
            ) {

        // 세션에서 사용자 이름 가져오기
        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;

        CommentResponseDto responseDto = commentService.createComment(scheduleId, requestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 특정 스케줄에 등록된 모든 댓글 목록을 조회합니다.
     *
     * @param scheduleId 댓글 목록을 조회할 대상 스케줄의 고유 ID.
     * @return           해당 스케줄의 댓글 목록 ({@link List}<{@link CommentResponseDto}>)과 HTTP 200 OK 상태 코드를 포함하는 {@link ResponseEntity}.
     */
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long scheduleId) {
        List<CommentResponseDto> responseDtos = commentService.getComments(scheduleId);
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * 특정 스케줄의 특정 댓글을 수정합니다.
     * 댓글을 작성한 사용자만 수정할 수 있습니다.
     *
     * @param scheduleId 수정할 댓글이 속한 스케줄의 고유 ID. (경로 일관성을 위해 사용)
     * @param commentId  수정할 댓글의 고유 ID.
     * @param requestDto 댓글 수정에 필요한 정보를 담은 DTO ({@link CommentRequestDto}). {@code @Valid} 어노테이션으로 유효성 검사가 수행됩니다.
     * @param request    HTTP 요청 객체. 세션에서 현재 로그인한 사용자의 username을 추출하여 권한을 확인하는 데 사용됩니다.
     * @return           수정된 댓글의 상세 정보를 담은 {@link CommentResponseDto}와 HTTP 200 OK 상태 코드를 포함하는 {@link ResponseEntity}.
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request) {

        // 세션에서 사용자이름 가져오기
        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;

        CommentResponseDto responseDto = commentService.updateComment(scheduleId, commentId, requestDto, username);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 특정 스케줄의 특정 댓글을 삭제합니다.
     * 댓글을 작성한 사용자만 삭제할 수 있습니다.
     *
     * @param scheduleId 삭제할 댓글이 속한 스케줄의 고유 ID. (경로 일관성을 위해 사용)
     * @param commentId  삭제할 댓글의 고유 ID.
     * @param request    HTTP 요청 객체. 세션에서 현재 로그인한 사용자의 username을 추출하여 권한을 확인하는 데 사용됩니다.
     * @return           삭제 성공 메시지 (예: {"msg": "댓글이 삭제되었습니다."})와 HTTP 200 OK 상태 코드를 포함하는 {@link ResponseEntity}.
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, String>> deleteComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            HttpServletRequest request
    ) {

        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;

        commentService.deleteComment(scheduleId, commentId, username);
        return ResponseEntity.ok(Map.of("msg", "댓글이 삭제되었습니다."));
    }

}
