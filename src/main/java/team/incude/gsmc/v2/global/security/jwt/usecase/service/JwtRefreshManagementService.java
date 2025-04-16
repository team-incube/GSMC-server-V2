package team.incude.gsmc.v2.global.security.jwt.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.global.security.jwt.persistence.repository.RefreshTokenRedisRepository;
import team.incude.gsmc.v2.global.security.jwt.usecase.JwtRefreshManagementUseCase;

@Service
@RequiredArgsConstructor
public class JwtRefreshManagementService implements JwtRefreshManagementUseCase {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Override
    public void deleteRefreshToken(String token) {
        refreshTokenRedisRepository.deleteById(token);
    }
}
