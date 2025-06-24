package team.incube.gsmc.v2.domain.auth.presentation.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 이메일 인증 코드 검증 요청에 사용되는 DTO입니다.
 * <p>이메일로 전송된 인증 코드를 클라이언트가 입력하여 서버에 검증을 요청할 때 사용됩니다.
 * @param code 사용자가 입력한 인증 코드 (8자리 숫자 문자열)
 * @author jihoonwjj
 */
public record VerificationCodeRequest(
        @NotBlank @Size(min=8, max=8) String code
) {
}
