package team.incube.gsmc.v2.domain.auth.persistence.mapper;

import org.springframework.stereotype.Component;
import team.incube.gsmc.v2.domain.auth.domain.Authentication;
import team.incube.gsmc.v2.domain.auth.persistence.entity.AuthenticationRedisEntity;
import team.incube.gsmc.v2.global.mapper.GenericMapper;

/**
 * 도메인 {@link Authentication} 객체와 Redis 엔티티 {@link AuthenticationRedisEntity} 간의 매핑을 수행하는 클래스입니다.
 * <p>{@link GenericMapper}를 구현하여 도메인 계층과 Redis 저장소 간의 객체 변환 책임을 가집니다.
 * 이메일 인증 상태의 저장 및 조회에 사용됩니다.
 * @author snowykte0426
 */
@Component
public class AuthenticationMapper implements GenericMapper<AuthenticationRedisEntity, Authentication> {

    /**
     * 도메인 {@link Authentication} 객체를 Redis 엔티티 {@link AuthenticationRedisEntity}로 변환합니다.
     * @param authentication 변환할 도메인 객체
     * @return Redis 저장용 엔티티 객체
     */
    @Override
    public AuthenticationRedisEntity toEntity(Authentication authentication) {
        return AuthenticationRedisEntity.builder()
                .email(authentication.getEmail())
                .attemptCount(authentication.getAttemptCount())
                .verified(authentication.getVerified())
                .ttl(authentication.getTtl())
                .build();
    }

    /**
     * Redis 엔티티 {@link AuthenticationRedisEntity}를 도메인 {@link Authentication} 객체로 변환합니다.
     * @param authenticationRedisEntity Redis에서 조회한 엔티티 객체
     * @return 도메인 객체
     */
    @Override
    public Authentication toDomain(AuthenticationRedisEntity authenticationRedisEntity) {
        return Authentication.builder()
                .email(authenticationRedisEntity.getEmail())
                .attemptCount(authenticationRedisEntity.getAttemptCount())
                .verified(authenticationRedisEntity.getVerified())
                .ttl(authenticationRedisEntity.getTtl())
                .build();
    }
}