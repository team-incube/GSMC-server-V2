package team.incude.gsmc.v2.domain.auth.presentation.data.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 이메일 인증 코드 발송 요청에 사용되는 DTO입니다.
 * <p>사용자가 입력한 이메일 주소를 기준으로 인증 코드를 전송합니다.
 * @param email 인증 메일을 받을 이메일 주소 (유효성 검사 포함: 이메일 형식, 3~50자)
 */
public record SendEmailRequest(
        @Email @NotBlank @Size(min=3, max=50) String email
) {
}