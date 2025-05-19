package team.incude.gsmc.v2.global.security.jwt.presentation.dto;

import java.time.LocalDateTime;

public record TokenDto(String token, LocalDateTime expiration) {
}
