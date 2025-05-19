package com.example.scheduler.service;

import com.example.scheduler.dto.schedule.SchedulePageResponseDto;
import com.example.scheduler.dto.schedule.ScheduleRequestDto;
import com.example.scheduler.dto.schedule.ScheduleResponseDto;
import com.example.scheduler.entity.Schedule;
import com.example.scheduler.entity.User;
import com.example.scheduler.repository.CommentRepository;
import com.example.scheduler.repository.ScheduleRepository;
import com.example.scheduler.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    // 일정 생성
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. " + username));

        Schedule schedule = new Schedule(requestDto.getTitle(), requestDto.getContent(), user);
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

    // 일정 페이지 조회
    public Page<SchedulePageResponseDto> getSchedulesWithPaging(int page, int size) {
        // 페이지 번호는 0부터 시작하므로 1을 전달받았다면 0으로 반환
        PageRequest pageable = PageRequest.of(page - 1, size);

        // 수정일 기준 내림차순 정렬된 일정 조회
        Page<Schedule> schedulePage = scheduleRepository.findAllByOrderByModifiedAtDesc(pageable);

        // DTO로 변환
        return schedulePage.map(schedule -> {
            long commnetCount = commentRepository.countByScheduleId(schedule.getId());
            return new SchedulePageResponseDto(schedule, commnetCount);
        });
    }
}
