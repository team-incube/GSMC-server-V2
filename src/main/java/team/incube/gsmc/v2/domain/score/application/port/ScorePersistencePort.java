package team.incube.gsmc.v2.domain.score.application.port;

import team.incube.gsmc.v2.domain.score.domain.Score;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.port.Port;

import java.util.List;

/**
 * 점수 도메인의 영속성 계층과의 통신을 담당하는 포트 인터페이스입니다.
 * <p>카테고리 이름 및 사용자 정보(이메일 또는 학생 코드)를 기반으로 점수를 조회하거나 저장하는 기능을 제공합니다.
 * <p>{@code @Port(direction = PortDirection.OUTBOUND)}로 선언되어 외부 저장소와 도메인 계층의 의존성을 분리합니다.
 * 주요 기능:
 * <ul>
 *   <li>점수 조회 (이메일/학생 코드 기반, 비관적 락 포함)</li>
 *   <li>다중 학생 코드 기반 점수 일괄 조회</li>
 *   <li>점수 저장</li>
 * </ul>
 * @author snowykte0426
 */
@Port(direction = PortDirection.OUTBOUND)
public interface ScorePersistencePort {
    Score findScoreByCategoryNameAndMemberEmailWithLock(String name, String email);

    List<Score> findScoreByMemberEmail(String email);

    List<Score> findScoreByStudentDetailStudentCodes(List<String> studentCodes);

    Score saveScore(Score score);
}