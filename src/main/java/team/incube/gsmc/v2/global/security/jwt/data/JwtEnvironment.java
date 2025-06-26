package team.incube.gsmc.v2.global.security.jwt.data;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
