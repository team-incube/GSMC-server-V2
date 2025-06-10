package team.incude.gsmc.v2.domain.score.application.usecase;

/**
 * 점수 수정 유스케이스를 정의하는 인터페이스입니다.
 * <p>사용자는 자신의 점수를 수정할 수 있으며, 관리자는 특정 학생의 점수를 수정할 수 있습니다.
 * 이 유스케이스는 점수 검증, 존재 여부 확인 및 저장을 포함한 수정 로직의 진입점을 제공합니다.
 * <ul>
 *   <li>{@code execute(String categoryName, Integer value)} - 현재 사용자 점수 수정</li>
 *   <li>{@code execute(String studentCode, String categoryName, Integer value)} - 특정 학생 점수 수정</li>
 * </ul>
 * @author snowykte0426
 */
public interface UpdateScoreUseCase {
    void execute(String categoryName, Integer value);

    void execute(String email, String categoryName, Integer value);
}