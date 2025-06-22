package team.incube.gsmc.v2.domain.auth.presentation.data.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 회원 가입 요청에 사용되는 DTO입니다.
 * <p>사용자의 이름, 이메일, 비밀번호를 입력받아 신규 회원을 등록합니다.
 * @param name 사용자 이름 (최대 20자)
 * @param email 사용자 이메일 (이메일 형식, 최대 50자)
 * @param password 사용자 비밀번호 (최대 30자)
 * @author jihoonwjj
 */
public record SignUpRequest(
        @NotBlank @Size(max=20) String name,
        @Email @NotBlank @Size(min=5, max=50) String email,
        @NotBlank @Size(max=30) String password
) {
}
