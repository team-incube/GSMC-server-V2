package team.incude.gsmc.v2.global.security.jwt.usecase;

public interface JwtRefreshManagementUseCase {
    void deleteRefreshToken(String refreshToken);
}
