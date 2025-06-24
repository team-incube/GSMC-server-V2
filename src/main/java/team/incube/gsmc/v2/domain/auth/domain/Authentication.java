package team.incube.gsmc.v2.domain.auth.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Authentication {
    private String email;
    private Integer attemptCount;
    private Boolean verified;
    private Long ttl;
}