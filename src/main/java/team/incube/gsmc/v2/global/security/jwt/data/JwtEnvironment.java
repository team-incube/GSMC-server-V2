package team.incube.gsmc.v2.global.security.jwt.data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT 환경 설정을 위한 데이터 클래스입니다.
 * <p>이 클래스는 액세스 토큰과 리프레시 토큰에 대한 설정을 포함하며, Spring Boot의 {@link ConfigurationProperties}를 사용하여
 * application.yml 또는 application.properties 파일에서 설정 값을 자동으로 매핑합니다.
 * <p>주요 필드로는 액세스 토큰과 리프레시 토큰의 비밀 키와 만료 시간을 포함합니다.
 * 이 설정을 통해 JWT 인증 및 인가를 위한 토큰 관리가 가능합니다.
 * @author jihoonwjj
 */
@ConfigurationProperties(prefix="jwt")
public record JwtEnvironment(

        AccessTokenProperties accessToken,
        RefreshTokenProperties refreshToken
) {
    public record AccessTokenProperties(
       String secret,
       long expiration
    ){}

    public record RefreshTokenProperties(
       String secret,
       long expiration
    ){}
}
