package team.incube.gsmc.v2.domain.auth.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthCode {
    private String email;
    private String authCode;
    private Long ttl;
}