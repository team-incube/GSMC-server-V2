package team.incude.gsmc.v2.global.security.jwt.service;

public interface JwtRefreshManagementService {
    void deleteRefreshToken(String refreshToken);
}
