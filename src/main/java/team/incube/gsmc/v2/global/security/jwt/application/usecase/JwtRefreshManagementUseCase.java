package team.incube.gsmc.v2.global.security.jwt.application.usecase;

public interface JwtRefreshManagementUseCase {
    void deleteRefreshToken(String refreshToken);
}
