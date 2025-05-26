package com.example.scheduler.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보 수정 요청 시 사용되는 데이터 전송 객체(DTO)입니다.
 * 사용자명, 이메일, 현재 비밀번호는 필수이며, 새 비밀번호는 선택적으로 입력받습니다.
 */
@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    /**
     * 수정할 사용자명 (닉네임)입니다.
     * 필수 입력값입니다.
     */
    @NotBlank(message = "사용자명은 필수 입력값입니다.")
    private String username;

    /**
     * 수정할 사용자 이메일 주소입니다.
     * 필수 입력값입니다.
     */
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    /**
     * 현재 사용자의 비밀번호입니다.
     * 정보 수정을 위해 현재 비밀번호 확인이 필요합니다.
     * 필수 입력값입니다.
     */
    @NotBlank(message = "현재 비밀번호는 필수 입력값입니다.")
    private String currentPassword;

    /**
     * 변경할 새 비밀번호입니다.
     * 입력하지 않으면 비밀번호는 변경되지 않습니다.
     * 입력 시 유효성 검증이 필요할 수 있습니다.
     */
    private String newPassword;

}
