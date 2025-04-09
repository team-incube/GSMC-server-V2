package team.incude.gsmc.v2.global.security.jwt.service;

public interface JwtRefreshManagement {
    void deleteRefreshToken(String refreshToken);
}
