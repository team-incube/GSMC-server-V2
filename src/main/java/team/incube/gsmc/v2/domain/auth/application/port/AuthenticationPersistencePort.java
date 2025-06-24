package team.incube.gsmc.v2.domain.auth.application.port;

import team.incube.gsmc.v2.domain.auth.domain.Authentication;

/**
 * 인증(Authentication) 정보를 관리하는 영속성 포트 인터페이스입니다.
 * <p>이 포트는 이메일 인증 여부 확인, 인증 객체 조회 및 저장 기능을 정의하며,
 * 도메인 계층이 인증 관련 데이터 저장소에 직접 의존하지 않도록 추상화합니다.
 * <ul>
 *   <li>{@code existsAuthenticationByEmail} - 이메일 기반 인증 여부 존재 확인</li>
 *   <li>{@code findAuthenticationByEmail} - 이메일을 통한 인증 객체 조회</li>
 *   <li>{@code saveAuthentication} - 인증 객체 저장</li>
 * </ul>
 * 실제 구현은 어댑터 계층에서 수행되며, 도메인 서비스에서 이 포트를 통해 인증 상태를 처리합니다.
 * @see Authentication
 * author jihoonwjj
 */
public interface AuthenticationPersistencePort {
    Boolean existsAuthenticationByEmail(String email);

    Authentication findAuthenticationByEmail(String email);

    Authentication saveAuthentication(Authentication authentication);
}
