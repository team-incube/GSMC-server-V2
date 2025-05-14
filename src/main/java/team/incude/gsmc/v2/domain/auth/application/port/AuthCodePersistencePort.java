package team.incude.gsmc.v2.domain.auth.application.port;

import team.incude.gsmc.v2.domain.auth.domain.AuthCode;

/**
 * 인증 코드(AuthCode)의 영속성 처리를 담당하는 포트 인터페이스입니다.
 * <p>이 포트는 이메일 인증을 위한 인증 코드의 생성, 조회, 삭제 등의 기능을 추상화하여
 * 도메인 계층이 데이터 저장소에 직접 의존하지 않도록 합니다.
 * <ul>
 *   <li>{@code existsAuthCodeByCode} - 인증 코드 존재 여부 확인</li>
 *   <li>{@code findAuthCodeByCode} - 인증 코드 조회</li>
 *   <li>{@code saveAuthCode} - 인증 코드 저장</li>
 *   <li>{@code deleteAuthCodeByCode} - 인증 코드 삭제</li>
 * </ul>
 * 실제 구현은 어댑터 계층에서 수행되며, 이 인터페이스는 도메인 계층과 어댑터 간의 경계를 정의합니다.
 * @see team.incude.gsmc.v2.domain.auth.domain.AuthCode
 * @author jihoonwjj
 */
public interface AuthCodePersistencePort {
    Boolean existsAuthCodeByCode(String code);

    AuthCode findAuthCodeByCode(String code);

    AuthCode saveAuthCode(AuthCode authCode);

    void deleteAuthCodeByCode(String code);
}
