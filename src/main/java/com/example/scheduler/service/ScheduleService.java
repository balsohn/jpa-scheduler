package com.example.scheduler.service;

import com.example.scheduler.dto.schedule.SchedulePageResponseDto;
import com.example.scheduler.dto.schedule.ScheduleRequestDto;
import com.example.scheduler.dto.schedule.ScheduleResponseDto;
import com.example.scheduler.entity.Schedule;
import com.example.scheduler.entity.User;
import com.example.scheduler.repository.CommentRepository;
import com.example.scheduler.repository.ScheduleRepository;
import com.example.scheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 스케줄 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 *
 * 이 서비스는 스케줄의 CRUD 작업과 페이징 처리를 담당하며,
 * 사용자 권한 검증과 데이터 유효성 검사를 수행합니다.
 */
@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    /**
     * 새로운 스케줄을 생성합니다.
     *
     * @param requestDto 생성할 스케줄의 정보 (제목, 내용)
     * @param username 스케줄을 생성하는 사용자명
     * @return 생성된 스케줄 정보를 담은 DTO
     * @throws IllegalArgumentException 사용자를 찾을 수 없는 경우
     *
     */
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. " + username));

        Schedule schedule = new Schedule(requestDto.getTitle(), requestDto.getContent(), user);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(savedSchedule);
    }

    /**
     * 등록된 모든 스케줄 목록을 조회합니다.
     *
     * @return 모든 스케줄 정보를 담은 {@link ScheduleResponseDto} 리스트.
     */
    public List<ScheduleResponseDto> getSchedules() {
        return scheduleRepository.findAll().stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 ID에 해당하는 스케줄의 상세 정보를 조회합니다.
     *
     * @param id 조회할 스케줄의 ID ({@link Schedule#getId()}).
     * @return 조회된 스케줄의 정보를 담은 {@link ScheduleResponseDto}.
     * @throws IllegalArgumentException 해당 ID의 스케줄을 찾을 수 없을 경우 발생.
     */
    public ScheduleResponseDto getSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다: " + id));
        return new ScheduleResponseDto(schedule);
    }

    /**
     * 스케줄을 수정합니다.
     *
     * 작성자만 자신의 스케줄을 수정할 수 있습니다.
     *
     * @param id 수정할 스케줄의 ID
     * @param requestDto 수정할 내용 (제목, 내용)
     * @param username 수정을 욫청한 사용자명
     * @return 수정된 스케줄 정보를 담은 DTO
     * @throws IllegalArgumentException 스케줄을 찾을 수 없거나 수정 권한이 없는 경우
     */
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

    /**
     * 특정 스케줄을 삭제합니다.
     * 스케줄을 작성한 사용자만 자신의 스케줄을 삭제할 수 있습니다.
     * 스케줄 삭제 시 연관된 댓글도 함께 삭제됩니다 ({@link Schedule} 엔티티의 cascade 설정에 의함).
     *
     * @param id       삭제할 스케줄의 ID ({@link Schedule#getId()}).
     * @param username 스케줄 삭제를 요청한 사용자의 이름 ({@link User#getUsername()}).
     * @throws IllegalArgumentException 해당 ID의 스케줄을 찾을 수 없거나, 삭제 권한이 없거나, {@code username}이 null인 경우 발생.
     */
    public void deleteSchedule(Long id, String username) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다. " + id));

        // 작성자만 삭제 가능
        if (!schedule.getUsername().equals(username)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        scheduleRepository.delete(schedule);
    }

    /**
     * 스케줄 목록을 페이징 처리하여 조회합니다.
     * 결과는 수정된 시간(modifiedAt)을 기준으로 내림차순 정렬됩니다.
     * 각 스케줄에는 연관된 댓글 수가 포함됩니다.
     *
     * @param page 조회할 페이지 번호 (1부터 시작).
     * @param size 한 페이지에 표시할 항목 수.
     * @return 페이징 처리된 스케줄 목록 ({@link Page}<{@link SchedulePageResponseDto}>).
     *         각 {@link SchedulePageResponseDto}는 {@link Schedule} 정보와 댓글 수를 포함합니다.
     */
    public Page<SchedulePageResponseDto> getSchedulesWithPaging(int page, int size) {
        // 페이지 번호는 0부터 시작하므로 1을 전달받았다면 0으로 반환
        PageRequest pageable = PageRequest.of(page - 1, size);

        // 수정일 기준 내림차순 정렬된 일정 조회
        Page<Schedule> schedulePage = scheduleRepository.findAllByOrderByModifiedAtDesc(pageable);

        // DTO로 변환
        return schedulePage.map(schedule -> {
            long commentCount = commentRepository.countByScheduleId(schedule.getId());
            return new SchedulePageResponseDto(schedule, commentCount);
        });
    }
}
