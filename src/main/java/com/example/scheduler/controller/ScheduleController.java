package com.example.scheduler.controller;

import com.example.scheduler.dto.schedule.ScheduleRequestDto;
import com.example.scheduler.dto.schedule.ScheduleResponseDto;
import com.example.scheduler.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @RequestBody ScheduleRequestDto requestDto,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("username");

        ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getSchedules() {
        List<ScheduleResponseDto> responseDtos = scheduleService.getSchedules();
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long id) {
        ScheduleResponseDto responseDto = scheduleService.getSchedule(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("username");

        ScheduleResponseDto responseDto = scheduleService.updateSchedule(id, requestDto, username);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSchedule(@PathVariable Long id,
                                                              HttpServletRequest request) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        scheduleService.deleteSchedule(id, username);
        return ResponseEntity.ok(Map.of("msg", "일정이 삭제되었습니다."));
    }
}
