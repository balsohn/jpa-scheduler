package com.example.scheduler.service;

import com.example.scheduler.dto.ScheduleRequestDto;
import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.entity.Schedule;
import com.example.scheduler.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // 일정 생성
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, String username) {
        Schedule schedule = new Schedule(requestDto.getTitle(), requestDto.getContent(), username);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(savedSchedule);
    }

    // 전체 일정 조회
    public List<ScheduleResponseDto> getSchedules() {
        return scheduleRepository.findAll().stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

    // 특정 일정 조회
    public ScheduleResponseDto getSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다: " + id));
        return new ScheduleResponseDto(schedule);
    }

    // 일정 수정
    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto, String username) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다. " + id));

        if (!schedule.getUsername().equals(username)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        schedule.update(requestDto.getTitle(), requestDto.getContent());
        return new ScheduleResponseDto(schedule);
    }

    // 일정 삭제
    public void deleteSchedule(Long id, String username) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다. " + id));

        // 작성자만 삭제 가능
        if (!schedule.getUsername().equals(username)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        scheduleRepository.delete(schedule);
    }
}
