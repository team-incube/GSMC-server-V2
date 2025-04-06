package team.incude.gsmc.v2.domain.auth.persistence.mapper;

import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.auth.domain.AuthCode;
import team.incude.gsmc.v2.domain.auth.persistence.entity.AuthCodeRedisEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

@Component
public class AuthCodeMapper implements GenericMapper<AuthCodeRedisEntity, AuthCode> {

    @Override
    public AuthCodeRedisEntity toEntity(AuthCode authCode) {
        return AuthCodeRedisEntity.builder()
                .email(authCode.getEmail())
                .authCode(authCode.getAuthCode())
                .ttl(authCode.getTtl())
                .build();
    }

    @Override
    public AuthCode toDomain(AuthCodeRedisEntity authCodeRedisEntity) {
        return AuthCode.builder()
                .email(authCodeRedisEntity.getEmail())
                .authCode(authCodeRedisEntity.getAuthCode())
                .ttl(authCodeRedisEntity.getTtl())
                .build();
    }
}