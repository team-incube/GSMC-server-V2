package team.incude.gsmc.v2.domain.score.application.usecase.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.application.usecase.FindScoreUseCase;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.presentation.data.GetScoreDto;
import team.incude.gsmc.v2.domain.score.presentation.data.response.GetScoreResponse;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

import java.util.List;

/**
 * 점수 조회 유스케이스의 구현체입니다.
 * <p>{@link FindScoreUseCase}를 구현하며, 현재 로그인한 사용자 또는 특정 학생의 점수를 조회하는 기능을 제공합니다.
 * <p>주요 처리 절차:
 * <ul>
 *   <li>이메일 또는 학생 코드로 학생 상세 정보 조회</li>
 *   <li>학생 코드에 해당하는 점수 목록 조회</li>
 *   <li>총점과 개별 점수를 포함한 응답 생성</li>
 * </ul>
 * @author snowykte0426
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindScoreService implements FindScoreUseCase {

    private final ScorePersistencePort scorePersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    /**
     * 현재 로그인한 사용자의 점수를 조회합니다.
     * @return 총합 점수 및 카테고리별 점수를 포함한 응답 객체
     */
    @Override
    public GetScoreResponse execute() {
        return findScore(studentDetailPersistencePort.findStudentDetailByMemberEmail(currentMemberProvider.getCurrentUser().getEmail()).getStudentCode());
    }

    /**
     * 주어진 학생 코드에 해당하는 점수를 조회합니다.
     * @param studentCode 점수를 조회할 학생의 고유 코드
     * @return 총합 점수 및 카테고리별 점수를 포함한 응답 객체
     */
    @Override
    public GetScoreResponse execute(String studentCode) {
        return findScore(studentCode);
    }

    /**
     * 내부적으로 점수를 조회하고 {@link GetScoreResponse} 형태로 변환합니다.
     * @param studentCode 학생 고유 코드
     * @return 총점과 상세 점수를 포함한 응답 객체
     */
    private GetScoreResponse findScore(String studentCode) {
        List<Score> scores = scorePersistencePort.findScoreByStudentDetailStudentCode(studentCode);
        return new GetScoreResponse(
                studentDetailPersistencePort.findTotalScoreByStudentCode(studentCode),
                scores.stream()
                        .map(score -> new GetScoreDto(
                                score.getCategory().getName(),
                                score.getValue()
                        )).toList()
        );
    }
}