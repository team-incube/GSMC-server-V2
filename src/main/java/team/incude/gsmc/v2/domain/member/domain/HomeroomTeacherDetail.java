package team.incude.gsmc.v2.domain.member.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomeroomTeacherDetail {
    private Long id;
    private Member member;
    private Integer grade;
    private Integer classNumber;
}