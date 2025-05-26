package com.example.scheduler.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 생성(회원가입) 요청 시 사용되는 데이터 전송 객체 (DTO) 입니다.
 * 각 필드에서 유효성 검증을 위한 어노테이션이 적용되어 있습니다.
 */
@Getter
@NoArgsConstructor
public class UserRequestDto {

    /**
     * 사용자명 (닉네임)
     * 필수 입력값이며, 최대 10자 입력가능
     */
    @NotBlank(message = "사용자명은 필수 입력값입니다.")
    @Size(max = 10, message = "사용자명은 최대 10자까지 입력 가능합니다.")
    private String username;

    /**
     * 사용자 이메일 주소.
     * 필수 입력값이며, 유효한 이메일 형식이어야 합니다.
     * 로그인 시 ID로 사용될 수 있으며, 시스템 내에서 고유해야 합니다.
     */
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    /**
     * 사용자 비밀번호.
     * 필수 입력값이며, 8자 이상 20자 이하로 입력해야 합니다.
     * 보안 강화를 위해 영문, 숫자, 특수문자가 각각 1개 이상 포함되어야 합니다.
     */
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
    message = "비밀번호는 영문, 숫자, 특수문자가 적어도 1개 이상 포함되어야 합니다.")
    private String password;
}
