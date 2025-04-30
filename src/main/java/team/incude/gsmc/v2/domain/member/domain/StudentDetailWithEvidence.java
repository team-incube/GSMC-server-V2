package team.incude.gsmc.v2.domain.member.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudentDetailWithEvidence {
    StudentDetail studentDetail;
    Boolean hasPendingEvidence;
}