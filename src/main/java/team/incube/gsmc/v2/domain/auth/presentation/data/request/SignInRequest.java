package team.incube.gsmc.v2.domain.auth.presentation.data.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 로그인 요청에 사용되는 DTO입니다.
 * <p>사용자가 입력한 이메일과 비밀번호를 기반으로 인증을 수행합니다.
 * @param email    로그인할 사용자 이메일 (이메일 형식, 최소 5자/최대 50자)
 * @param password 로그인할 사용자 비밀번호 (최소 8자/최대 30자)
 * @author jihoonwjj
 */
public record SignInRequest(
        @Email @NotBlank @Size(min = 5, max = 50) String email,
        @NotBlank @Size(min = 8, max = 30) String password
) {
}