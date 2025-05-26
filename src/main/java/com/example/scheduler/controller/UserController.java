package com.example.scheduler.controller;

import com.example.scheduler.dto.user.LoginRequestDto;
import com.example.scheduler.dto.user.UserRequestDto;
import com.example.scheduler.dto.user.UserResponseDto;
import com.example.scheduler.dto.user.UserUpdateRequestDto;
import com.example.scheduler.entity.User;
import com.example.scheduler.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 사용자 관리를 위한 REST API 컨트롤러 입니다.
 *
 * 이 컨트롤러는 사용자의 생성(회원가입), 조회, 수정, 삭제 및 로그인 기능을 제공합니다.
 *
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * 새로운 사용자를 생성합니다 (회원가입)
     *
     * @param requestDto 사용자 생성에 필요한 정보를 담은 DTO
     * @return           생성된 사용자의 정보를 담은 {@link UserResponseDto}와 HTTP 201 Created 상태 코드를 포함하는 {@link ResponseEntity}
     */
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto requestDto) {
        UserResponseDto responseDto = userService.createUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 등록된 모든 사용자 목록을 조회합니다.
     *
     * @return 전체 사용자 목록 List<{@link UserResponseDto}>과 HTTP 200 OK 상태 코드를 포함하는 {@link ResponseEntity}
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<UserResponseDto> responseDtos = userService.getUsers();
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * 특정 ID에 해당하는 사용자의 정보를 조회합니다.
     *
     * @param id 조회할 사용자의 고유 ID
     * @return   조회된 ㅏ용자의 정보 {@link UserResponseDto}와 HTTP 200 OK 상태 코드를 포함하는 {@link ResponseEntity}
     *           해당 ID의 사용자가 없을경우 서비스 레이어에서 예외가 발생할 수 있습니다.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        UserResponseDto responseDto = userService.getUser(id);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 특정 ID에 해당하는 사용자의 정보를 수정합니다.
     * 일반적으로 자신의 정보만 수정 가능하도록 하거나, 관리자 권한이 필요할 수 있습니다.
     *
     * @param id           수정할 사용자의 고유 ID
     * @param requestDto   사용자 정보 수정에 필요한 정보를 담은 DTO
     * @param request      HTTP 요청객체. 사용자 인증/인가 정보를 확인하는 데 사용될 수 있습니다.
     * @return             수정된 사용자의 정보 {@link UserResponseDto} 와 HTTP 200 OK 상태 코드를 포함하는 {@link ResponseEntity}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDto requestDto,
            HttpServletRequest request) {
        UserResponseDto responseDto = userService.updateUser(id, requestDto, request);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 특정 ID에 해당하는 사용자를 삭제합니다. (회원 탈퇴)
     * 일반적으로 자신의 계정만 삭제 가능하도록 하거나, 관리자 권한이 필요합니다.
     *
     * @param id    삭제할 사용자의 고유 ID
     * @return      삭제 성공메세지와 HTTP 200 OK 상태코드를 포함하는 {@link ResponseEntity}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("msg", "사용자가 삭제되었습니다."));
    }

    /**
     * 사용자 로그인을 처리합니다.
     *
     * @param requestDto    로그인에 필요한 사용자명(또는 이메일)과 비밀번호를 담은 DTO
     * @param request       HTTP 요청객체. 세션 관리에 사용될 수 있습니다.
     * @param response      HTTP 요청객체. 쿠키 설정 또는 헤더 추가 등에 사용될 수 있습니다.
     * @return              로그인 성공 메세지와 HTTP 200 OK 상태 코드를 포함하는 {@link ResponseEntity}
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody LoginRequestDto requestDto,
            HttpServletRequest request,
            HttpServletResponse response
            ) {
        userService.login(requestDto, request, response);
        return ResponseEntity.ok(Map.of("msg", "로그인 성공"));

    }
}
