package team.incube.gsmc.v2.domain.score.domain;

import lombok.Builder;
import lombok.Getter;
import team.incube.gsmc.v2.domain.member.domain.Member;

@Getter
@Builder
public class Score {
    private Long id;
    private Member member;
    private Category category;
    private Integer value;

    public void plusValue(Integer value) {
        this.value += value;
    }

    public void minusValue(Integer value) {
        this.value -= value;
    }
}