package team.incube.gsmc.v2.domain.member.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudentDetailWithEvidence {
    StudentDetail studentDetail;
    Boolean hasPendingEvidence;
}