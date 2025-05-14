package team.incude.gsmc.v2.domain.auth.presentation.data.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 비밀번호 변경 요청에 사용되는 DTO입니다.
 * <p>사용자의 이메일과 새 비밀번호를 전달받아 비밀번호를 갱신합니다.
 * <ul>
 *   <li>{@code email} - 변경 대상 사용자의 이메일 주소 (유효성 검증 포함)</li>
 *   <li>{@code password} - 새 비밀번호 (8자 이상 20자 이하)</li>
 * </ul>
 * 이 요청은 인증이 필요한 사용자가 자신의 비밀번호를 갱신할 때 사용됩니다.
 */
public record ChangePasswordRequest(
        @Email @NotBlank @Size(min=3, max=50) String email,
        @NotBlank @Size(min=8, max=20) String password
) {
}