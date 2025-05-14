package team.incude.gsmc.v2.domain.auth.persistence.mapper;

import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.auth.domain.AuthCode;
import team.incude.gsmc.v2.domain.auth.persistence.entity.AuthCodeRedisEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

/**
 * 도메인 {@link AuthCode} 객체와 Redis 엔티티 {@link AuthCodeRedisEntity} 간의 매핑을 수행하는 클래스입니다.
 * <p>{@link GenericMapper}를 구현하여 인증 코드 도메인 모델과 Redis 저장소 간의 변환 책임을 가집니다.
 * 인증 코드의 저장, 조회, 검증 작업에 필요한 데이터 전환을 담당합니다.
 * @author snowykte0426
 */
@Component
public class AuthCodeMapper implements GenericMapper<AuthCodeRedisEntity, AuthCode> {

    /**
     * 도메인 {@link AuthCode} 객체를 Redis 저장용 {@link AuthCodeRedisEntity}로 변환합니다.
     * @param authCode 변환할 도메인 인증 코드 객체
     * @return Redis 저장소에 저장 가능한 엔티티 객체
     */
    @Override
    public AuthCodeRedisEntity toEntity(AuthCode authCode) {
        return AuthCodeRedisEntity.builder()
                .email(authCode.getEmail())
                .authCode(authCode.getAuthCode())
                .ttl(authCode.getTtl())
                .build();
    }

    /**
     * Redis에 저장된 {@link AuthCodeRedisEntity}를 도메인 {@link AuthCode} 객체로 변환합니다.
     * @param authCodeRedisEntity Redis에서 조회한 인증 코드 엔티티
     * @return 도메인 인증 코드 객체
     */
    @Override
    public AuthCode toDomain(AuthCodeRedisEntity authCodeRedisEntity) {
        return AuthCode.builder()
                .email(authCodeRedisEntity.getEmail())
                .authCode(authCodeRedisEntity.getAuthCode())
                .ttl(authCodeRedisEntity.getTtl())
                .build();
    }
}