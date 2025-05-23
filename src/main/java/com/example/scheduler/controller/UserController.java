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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 유저 생성
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto requestDto) {
        UserResponseDto responseDto = userService.createUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 전체 유저 조회
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<UserResponseDto> responseDtos = userService.getUsers();
        return ResponseEntity.ok(responseDtos);
    }

    // 특정 유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        UserResponseDto responseDto = userService.getUser(id);
        return ResponseEntity.ok(responseDto);
    }

    // 유저 수정
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDto requestDto,
            HttpServletRequest request) {
        UserResponseDto responseDto = userService.updateUser(id, requestDto, request);
        return ResponseEntity.ok(responseDto);
    }

    // 유저 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("msg", "사용자가 삭제되었습니다."));
    }

    // 로그인
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
