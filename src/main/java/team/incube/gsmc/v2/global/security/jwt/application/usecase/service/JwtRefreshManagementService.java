package team.incube.gsmc.v2.global.security.jwt.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incube.gsmc.v2.global.security.jwt.persistence.repository.RefreshTokenRedisRepository;
import team.incube.gsmc.v2.global.security.jwt.application.usecase.JwtRefreshManagementUseCase;

/**
 * 리프레시 토큰 관리 기능을 제공하는 유스케이스 구현 클래스입니다.
 * <p>{@link JwtRefreshManagementUseCase}를 구현하며,
 * Redis에서 리프레시 토큰을 삭제하는 기능을 수행합니다.
 * <p>주요 역할:
 * <ul>
 *   <li>로그아웃 시 토큰 무효화</li>
 *   <li>만료 또는 재사용 방지를 위한 토큰 삭제</li>
 * </ul>
 * <p>내부적으로 Redis 저장소를 사용하여 리프레시 토큰을 관리합니다.
 * @author jihoonwjj
 */

@Service
@RequiredArgsConstructor
public class JwtRefreshManagementService implements JwtRefreshManagementUseCase {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Override
    public void deleteRefreshToken(String token) {
        refreshTokenRedisRepository.deleteById(token);
    }
}
