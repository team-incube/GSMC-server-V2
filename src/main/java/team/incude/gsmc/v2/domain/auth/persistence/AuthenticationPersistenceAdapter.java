package team.incude.gsmc.v2.domain.auth.persistence;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incude.gsmc.v2.domain.auth.domain.Authentication;
import team.incude.gsmc.v2.domain.auth.exception.AuthenticationNotFoundException;
import team.incude.gsmc.v2.domain.auth.persistence.mapper.AuthenticationMapper;
import team.incude.gsmc.v2.domain.auth.persistence.repository.AuthenticationRedisRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

/**
 * 이메일 인증 상태를 Redis 저장소에 연결하는 어댑터 클래스입니다.
 * <p>{@link AuthenticationPersistencePort}를 구현하며, 이메일 인증 여부 확인, 인증 정보 저장 및 조회를 Redis를 통해 수행합니다.
 * {@link AuthenticationMapper}를 통해 도메인 객체와 Redis 엔티티 간 매핑을 처리합니다.
 * 이 어댑터는 도메인 계층이 Redis 구현체에 직접 의존하지 않도록 추상화된 접근을 제공합니다.
 * @author jihoonwjj
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class AuthenticationPersistenceAdapter implements AuthenticationPersistencePort {

    private final AuthenticationRedisRepository authenticationRedisRepository;
    private final AuthenticationMapper authenticationMapper;

    /**
     * 주어진 이메일을 가진 인증 객체가 Redis에 존재하는지 여부를 반환합니다.
     * @param email 확인할 이메일 주소
     * @return 인증 객체 존재 여부
     */
    @Override
    public Boolean existsAuthenticationByEmail(String email) {
        return authenticationRedisRepository.existsById(email);
    }

    /**
     * 이메일을 기반으로 Redis에서 인증 객체를 조회합니다.
     * @param email 조회할 이메일 주소
     * @return 조회된 인증 도메인 객체
     * @throws AuthenticationNotFoundException 인증 정보가 존재하지 않을 경우
     */
    @Override
    public Authentication findAuthenticationByEmail(String email) {
        return authenticationMapper.toDomain(authenticationRedisRepository.findByEmail(email).orElseThrow(AuthenticationNotFoundException::new));
    }

    /**
     * 주어진 인증 객체를 Redis에 저장합니다.
     * @param authentication 저장할 인증 도메인 객체
     * @return 저장된 인증 도메인 객체
     */
    @Override
    public Authentication saveAuthentication(Authentication authentication) {
        return authenticationMapper.toDomain(authenticationRedisRepository.save(authenticationMapper.toEntity(authentication)));
    }
}