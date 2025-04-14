package team.incude.gsmc.v2.domain.auth.persistence;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.auth.application.port.AuthCodePersistencePort;
import team.incude.gsmc.v2.domain.auth.domain.AuthCode;
import team.incude.gsmc.v2.domain.auth.persistence.mapper.AuthCodeMapper;
import team.incude.gsmc.v2.domain.auth.persistence.repository.AuthCodeRedisRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class AuthCodePersistenceAdapter implements AuthCodePersistencePort {

    private final AuthCodeRedisRepository authCodeRedisRepository;
    private final AuthCodeMapper authCodeMapper;

    @Override
    public void saveAuthCode(AuthCode authCode) {
        authCodeRedisRepository.save(authCodeMapper.toEntity(authCode));
    }

    @Override
    public Boolean existsAuthCodeByCode(String code) {
        return authCodeRedisRepository.existsByCode(code);
    }

    @Override
    public AuthCode findAuthCodeByCode(String code) {
        return authCodeMapper.toDomain(authCodeRedisRepository.findByAuthCode(code));
    }

    @Override
    public void deleteAuthCodeByCode(String code) {
        authCodeRedisRepository.deleteByCode(code);
    }
}
