package team.incude.gsmc.v2.domain.auth.presentation.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 액세스 토큰 갱신을 위한 요청 DTO입니다.
 * <p>클라이언트는 만료되었거나 곧 만료될 액세스 토큰을 갱신하기 위해 리프레시 토큰을 함께 전송합니다.
 * @param refreshToken 서버에 저장된 리프레시 토큰과 일치해야 하는 토큰 값 (최대 256자)
 * @author jihoonwjj
 */
public record RefreshRequest (
        @NotBlank @Size(max=256) String refreshToken
){
}