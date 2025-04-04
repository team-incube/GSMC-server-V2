package team.incude.gsmc.v2.domain.auth.persistence.mapper;

import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.auth.domain.Authentication;
import team.incude.gsmc.v2.domain.auth.persistence.entity.AuthCodeRedisEntity;
import team.incude.gsmc.v2.domain.auth.persistence.entity.AuthenticationRedisEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

@Component
public class AuthenticationMapper implements GenericMapper<AuthenticationRedisEntity, Authentication> {

    @Override
    public AuthenticationRedisEntity toEntity(Authentication authentication) {
        return AuthenticationRedisEntity.builder()
                .email(authentication.getEmail())
                .attemptCount(authentication.getAttemptCount())
                .verified(authentication.getVerified())
                .ttl(authentication.getTtl())
                .build();
    }

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