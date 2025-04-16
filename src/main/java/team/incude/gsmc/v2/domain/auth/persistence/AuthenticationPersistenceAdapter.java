package team.incude.gsmc.v2.domain.auth.persistence;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incude.gsmc.v2.domain.auth.domain.Authentication;
import team.incude.gsmc.v2.domain.auth.exception.AuthenticationNotFoundException;
import team.incude.gsmc.v2.domain.auth.persistence.mapper.AuthenticationMapper;
import team.incude.gsmc.v2.domain.auth.persistence.repository.AuthenticationRedisRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class AuthenticationPersistenceAdapter implements AuthenticationPersistencePort {

    private final AuthenticationRedisRepository authenticationRedisRepository;
    private final AuthenticationMapper authenticationMapper;

    @Override
    public Boolean existsAuthenticationByEmail(String email) {
        return authenticationRedisRepository.existsById(email);
    }

    @Override
    public Authentication findAuthenticationByEmail(String email) {
        return authenticationMapper.toDomain(authenticationRedisRepository.findByEmail(email).orElseThrow(AuthenticationNotFoundException::new));
    }

    @Override
    public Authentication saveAuthentication(Authentication authentication) {
        return authenticationMapper.toDomain(authenticationRedisRepository.save(authenticationMapper.toEntity(authentication)));
    }
}
