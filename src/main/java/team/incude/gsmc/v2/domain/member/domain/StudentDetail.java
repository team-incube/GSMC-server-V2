package team.incude.gsmc.v2.domain.member.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudentDetail {
    private Long id;
    private Member member;
    private Integer grade;
    private Integer classNumber;
    private Integer number;
    private Integer totalScore;
    private Integer studentCode;
}