package team.incude.gsmc.v2.domain.certificate.domain;

import lombok.Builder;
import lombok.Getter;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.member.domain.Member;

import java.time.LocalDate;

@Getter
@Builder
public class Certificate {
    private Long id;
    private Member member;
    private OtherEvidence evidence;
    private String name;
    private LocalDate acquisitionDate;
}