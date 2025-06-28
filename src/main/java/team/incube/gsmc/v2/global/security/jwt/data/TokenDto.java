package team.incube.gsmc.v2.global.security.jwt.data;

import java.time.LocalDateTime;

/**
 * JWT 토큰 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * <p>이 클래스는 JWT 토큰과 해당 토큰의 만료 시간을 포함하며, 클라이언트와 서버 간의 데이터 전송에 사용됩니다.
 * @param token JWT 토큰 문자열
 * @param expiration 토큰의 만료 시간
 * @author jihoonwjj
 */
public record TokenDto(String token, LocalDateTime expiration) {
}