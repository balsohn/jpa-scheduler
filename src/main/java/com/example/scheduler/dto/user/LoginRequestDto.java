package com.example.scheduler.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 로그인 요청 시 사용되는 데이터 전송 객체(DTO)입니다.
 * 이메일과 비밀번호 필드에 대한 유효성 검증을 포함합니다.
 */
@Getter
@NoArgsConstructor
public class LoginRequestDto {

    /**
     * 사용자 로그인 이메일 주소입니다.
     * 필수 입력값이며, 유효한 이메일 형식이어야 합니다.
     */
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    /**
     * 사용자 로그인 비밀번호입니다.
     * 필수 입력값입니다.
     */
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
