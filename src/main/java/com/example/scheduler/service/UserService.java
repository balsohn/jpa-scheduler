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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 회원가입, 사용자 정보 조회, 수정, 삭제, 로그인 기능을 담당합니다.
 */

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 새로운 사용자를 생성합니다 (회원가입).
     * 요청된 이메일과 사용자명이 이미 존재하는지 확인하고, 비밀번호를 암호화하여 저장합니다.
     *
     * @param requestDto 사용자 생성에 필요한 정보를 담은 {@link UserRequestDto}
     * @return 생성된 사용자의 정보를 담은 {@link UserResponseDto}
     * @throws IllegalArgumentException 이미 존재하는 이메일 또는 사용자명일 경우 발생
     */
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

    /**
     * 등록된 모든 사용자 목록을 조회합니다.
     * 각 사용자 엔티티를 {@link UserResponseDto}로 변환하여 반환합니다.
     *
     * @return 모든 사용자의 정보를 담은 {@link UserResponseDto} 리스트
     */
    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 ID에 해당하는 사용자의 정보를 조회합니다.
     *
     * @param id 조회할 사용자의 고유 ID
     * @return 조회된 사용자의 정보를 담은 {@link UserResponseDto}
     * @throws IllegalArgumentException 해당 ID의 사용자를 찾을 수 없을 경우 발생
     */
    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. " + id));
        return new UserResponseDto(user);
    }

    /**
     * 특정 사용자의 정보를 수정합니다.
     * 이 작업은 트랜잭션 내에서 수행됩니다.
     * 수정 요청을 한 사용자가 대상 계정의 소유주인지, 현재 비밀번호가 일치하는지 확인합니다.
     * 변경하려는 이메일이 다른 사용자에 의해 이미 사용 중인지도 확인합니다.
     * 새로운 비밀번호가 제공된 경우 비밀번호도 업데이트합니다.
     *
     * @param id         수정할 사용자의 고유 ID
     * @param requestDto 사용자 정보 수정에 필요한 정보를 담은 {@link UserUpdateRequestDto}
     * @param request    HTTP 요청 객체, 세션에서 현재 로그인한 사용자 정보를 가져오기 위해 사용됩니다.
     * @return 수정된 사용자의 정보를 담은 {@link UserResponseDto}
     * @throws IllegalArgumentException 사용자를 찾을 수 없거나, 권한이 없거나, 현재 비밀번호가 틀리거나, 이메일이 중복될 경우 발생
     */
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

    /**
     * 특정 ID에 해당하는 사용자를 삭제합니다 (회원 탈퇴).
     * <p><strong>주의:</strong> 이 메소드는 현재 요청한 사용자의 권한을 확인하지 않습니다.
     * 컨트롤러 단이나 이 메소드 내에서 삭제 권한(예: 본인 또는 관리자)을 확인하는 로직이 추가되어야 합니다.</p>
     *
     * @param id 삭제할 사용자의 고유 ID
     * @throws IllegalArgumentException 해당 ID의 사용자를 찾을 수 없을 경우 발생
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. " + id));

        userRepository.delete(user);
    }

    /**
     * 사용자 로그인을 처리합니다.
     * 제공된 이메일로 사용자를 찾고, 비밀번호가 일치하는지 확인합니다.
     * 로그인 성공 시, HTTP 세션에 사용자 ID와 사용자명을 저장합니다.
     *
     * @param requestDto 로그인에 필요한 이메일과 비밀번호를 담은 {@link LoginRequestDto}
     * @param request    HTTP 요청 객체, 세션 생성을 위해 사용됩니다.
     * @param response   HTTP 응답 객체 (현재 코드에서는 직접 사용되지 않지만, JWT 토큰 등을 쿠키에 설정할 때 사용될 수 있습니다).
     * @throws IllegalArgumentException 등록된 사용자가 없거나 비밀번호가 일치하지 않을 경우 발생
     */
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
