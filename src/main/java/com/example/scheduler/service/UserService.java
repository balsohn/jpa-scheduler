package com.example.scheduler.service;

import com.example.scheduler.config.PasswordEncoder;
import com.example.scheduler.dto.user.LoginRequestDto;
import com.example.scheduler.dto.user.UserRequestDto;
import com.example.scheduler.dto.user.UserResponseDto;
import com.example.scheduler.dto.user.UserUpdateRequestDto;
import com.example.scheduler.entity.User;
import com.example.scheduler.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 유저 생성
    public UserResponseDto createUser(UserRequestDto requestDto) {
        // 이메일 중복 체크
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 사용자명 중복 체크 추가
        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다.");
        }

        // 비밀번호 암호화 적용
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(requestDto.getUsername(), requestDto.getEmail(), encodedPassword);
        User savedUser = userRepository.save(user);

        return new UserResponseDto(savedUser);
    }

    // 전체 유저 조회
    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    // 특정 유저 조회
    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. " + id));
        return new UserResponseDto(user);
    }

    // 유저 수정
    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto requestDto, HttpServletRequest request) {
        // 사용자 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. " + id));

        // 자신의 계정만 수정 가능하도록 체크
        HttpSession session = request.getSession();
        String sessionUsername = (String) session.getAttribute("username");
        if (!user.getUsername().equals(sessionUsername)) {
            throw new IllegalArgumentException("본인의 계정만 수정할 수 없습니다.");
        }

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 이메일 중복 체크
        userRepository.findByEmail(requestDto.getEmail())
                .ifPresent(existingUser -> {
                    if(!existingUser.getId().equals(id)) {
                        throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
                    }
                });

        user.update(requestDto.getUsername(), requestDto.getEmail());

        // 새 비밀번호가 입력된 경우 비밀번호 업데이트
        if (requestDto.getNewPassword() != null && !requestDto.getNewPassword().isEmpty()) {
            String encodePassword = passwordEncoder.encode(requestDto.getNewPassword());
            user.updatePassword(encodePassword);
        }
        return new UserResponseDto(user);
    }

    // 유저 삭제
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. " + id));

        userRepository.delete(user);
    }

    // 로그인
    public void login(LoginRequestDto requestDto, HttpServletRequest request, HttpServletResponse response) {
        // 사용자 확인
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        // 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 세션 생성 및 사용자 정보 저장
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
    }
}
