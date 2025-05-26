package com.example.scheduler.controller;

import com.example.scheduler.dto.schedule.SchedulePageResponseDto;
import com.example.scheduler.dto.schedule.ScheduleRequestDto;
import com.example.scheduler.dto.schedule.ScheduleResponseDto;
import com.example.scheduler.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 스케쥴 관리를 위한 REST API 컨트롤러입니다.
 *
 * 이 컨트롤러는 스케줄의 생성, 조회, 수정, 삭제 및 페이징 처리된 조회 기능을 제공합니다.
 * 모든 요청은 인증된 사용자만 접근 가능합니다.
 *
 * @author 손지호
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 새로운 스케줄을 생성합니다.
     *
     * @param requestDto 스케줄 생성에 필요한 정보를 담은 DTO ({@link ScheduleRequestDto}). {@code @Valid} 어노테이션으로 유효성 검사가 수행됩니다.
     * @param request    HTTP 요청 객체. 세션에서 현재 로그인한 사용자의 username을 추출하는 데 사용됩니다.
     * @return           생성된 스케줄의 상세 정보를 담은 {@link ScheduleResponseDto}와 HTTP 201 Created 상태 코드를 포함하는 {@link ResponseEntity}.
     */
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @Valid @RequestBody ScheduleRequestDto requestDto,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("username");

        ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 등록된 모든 스케줄 목록을 조회합니다.
     *
     * @return 전체 스케줄 목록 ({@link List}<{@link ScheduleResponseDto}>)과 HTTP 200 OK 상태 코드를 포함하는 {@link ResponseEntity}.
     */
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getSchedules() {
        List<ScheduleResponseDto> responseDtos = scheduleService.getSchedules();
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * 특정 ID에 해당하는 스케줄의 상세 정보를 조회합니다.
     *
     * @param id 조회할 스케줄의 고유 ID ({@link com.example.scheduler.entity.Schedule#getId()}).
     * @return   조회된 스케줄의 상세 정보 ({@link ScheduleResponseDto})와 HTTP 200 OK 상태 코드를 포함하는 {@link ResponseEntity}.
     *           해당 ID의 스케줄이 없을 경우 서비스 레이어에서 예외가 발생할 수 있습니다.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long id) {
        ScheduleResponseDto responseDto = scheduleService.getSchedule(id);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 특정 ID에 해당하는 스케줄을 수정합니다.
     * 스케줄을 작성한 사용자만 수정할 수 있습니다.
     *
     * @param id         수정할 스케줄의 고유 ID ({@link com.example.scheduler.entity.Schedule#getId()}).
     * @param requestDto 스케줄 수정에 필요한 정보를 담은 DTO ({@link ScheduleRequestDto}). {@code @Valid} 어노테이션으로 유효성 검사가 수행됩니다.
     * @param request    HTTP 요청 객체. 세션에서 현재 로그인한 사용자의 username을 추출하여 권한을 확인하는 데 사용됩니다.
     * @return           수정된 스케줄의 상세 정보 ({@link ScheduleResponseDto})와 HTTP 200 OK 상태 코드를 포함하는 {@link ResponseEntity}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleRequestDto requestDto,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("username");

        ScheduleResponseDto responseDto = scheduleService.updateSchedule(id, requestDto, username);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 특정 ID에 해당하는 스케줄을 삭제합니다.
     * 스케줄을 작성한 사용자만 삭제할 수 있습니다.
     *
     * @param id      삭제할 스케줄의 고유 ID ({@link com.example.scheduler.entity.Schedule#getId()}).
     * @param request HTTP 요청 객체. 세션에서 현재 로그인한 사용자의 username을 추출하여 권한을 확인하는 데 사용됩니다.
     * @return        삭제 성공 메시지 (예: {"msg": "일정이 삭제되었습니다."})와 HTTP 200 OK 상태 코드를 포함하는 {@link ResponseEntity}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSchedule(@PathVariable Long id,
                                                              HttpServletRequest request) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        scheduleService.deleteSchedule(id, username);
        return ResponseEntity.ok(Map.of("msg", "일정이 삭제되었습니다."));
    }

    /**
     * 스케줄 목록을 페이징 처리하여 조회합니다.
     *
     * @param page 조회할 페이지 번호 (1부터 시작, 기본값: 1).
     * @param size 한 페이지에 표시할 항목 수 (기본값: 10).
     * @return     페이징 처리된 스케줄 목록 ({@link Page}<{@link SchedulePageResponseDto}>)과 HTTP 200 OK 상태 코드를 포함하는 {@link ResponseEntity}.
     */
    @GetMapping("/paging")
    public ResponseEntity<Page<SchedulePageResponseDto>> getSchedulesWithPaging(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<SchedulePageResponseDto> responseDtos = scheduleService.getSchedulesWithPaging(page, size);
        return ResponseEntity.ok(responseDtos);
    }
}
