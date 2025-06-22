package team.incube.gsmc.v2.domain.auth.persistence;

import lombok.RequiredArgsConstructor;
import team.incube.gsmc.v2.domain.auth.application.port.AuthCodePersistencePort;
import team.incube.gsmc.v2.domain.auth.domain.AuthCode;
import team.incube.gsmc.v2.domain.auth.persistence.mapper.AuthCodeMapper;
import team.incube.gsmc.v2.domain.auth.persistence.repository.AuthCodeRedisRepository;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;

/**
 * 인증 코드 도메인을 Redis 기반 저장소와 연결하는 어댑터 클래스입니다.
 * <p>{@link AuthCodePersistencePort}를 구현하며, 인증 코드의 저장, 조회, 존재 확인, 삭제 기능을 Redis에 위임합니다.
 * {@link AuthCodeMapper}를 통해 도메인 객체와 Redis 엔티티 간의 변환을 수행합니다.
 * 이 클래스는 도메인 계층이 Redis 저장소 구현에 직접 의존하지 않도록 분리된 구조를 제공합니다.
 * @author jihoonwjj
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class AuthCodePersistenceAdapter implements AuthCodePersistencePort {

    private final AuthCodeRedisRepository authCodeRedisRepository;
    private final AuthCodeMapper authCodeMapper;

    /**
     * 인증 코드를 Redis에 저장합니다.
     * @param authCode 저장할 인증 코드 도메인 객체
     * @return 저장된 인증 코드 도메인 객체
     */
    @Override
    public AuthCode saveAuthCode(AuthCode authCode) {
        return authCodeMapper.toDomain(authCodeRedisRepository.save(authCodeMapper.toEntity(authCode)));
    }

    /**
     * 주어진 인증 코드가 Redis에 존재하는지 확인합니다.
     * @param code 인증 코드 문자열
     * @return 존재 여부
     */
    @Override
    public Boolean existsAuthCodeByCode(String code) {
        return authCodeRedisRepository.existsByAuthCode(code);
    }

    /**
     * 인증 코드를 기준으로 Redis에서 인증 정보를 조회합니다.
     * @param code 인증 코드 문자열
     * @return 조회된 인증 코드 도메인 객체
     */
    @Override
    public AuthCode findAuthCodeByCode(String code) {
        return authCodeMapper.toDomain(authCodeRedisRepository.findByAuthCode(code));
    }

    /**
     * 인증 코드를 기준으로 Redis에서 인증 정보를 삭제합니다.
     * @param code 삭제할 인증 코드 문자열
     */
    @Override
    public void deleteAuthCodeByCode(String code) {
        authCodeRedisRepository.deleteByAuthCode(code);
    }
}