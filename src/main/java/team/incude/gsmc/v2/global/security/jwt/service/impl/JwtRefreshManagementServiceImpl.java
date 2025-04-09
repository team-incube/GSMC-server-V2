package team.incude.gsmc.v2.global.security.jwt.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.global.security.jwt.repository.RefreshTokenRedisRepository;
import team.incude.gsmc.v2.global.security.jwt.service.JwtRefreshManagement;

@Service
@RequiredArgsConstructor
public class JwtRefreshManagementServiceImpl implements JwtRefreshManagement {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Override
    public void deleteRefreshToken(String token) {
        refreshTokenRedisRepository.deleteById(token);
    }
}
