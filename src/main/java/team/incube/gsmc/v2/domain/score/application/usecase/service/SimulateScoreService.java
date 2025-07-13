package team.incube.gsmc.v2.domain.score.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incube.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incube.gsmc.v2.domain.score.application.usecase.SimulateScoreUseCase;
import team.incube.gsmc.v2.domain.score.domain.Category;
import team.incube.gsmc.v2.domain.score.presentation.data.response.GetScoreSimulateResponse;
import team.incube.gsmc.v2.global.util.SimulateScoreUtil;
import team.incube.gsmc.v2.global.util.SnakeKebabToCamelCaseConverterUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 모의 점수 계산 유스케이스의 구현체입니다.
 * <p>{@link SimulateScoreUseCase}를 구현하며, 입력된 전공,인문/인성,외국어 항목을 기반으로 {@link SimulateScoreUtil}을 통해 점수를 계산하고 반환합니다.
 * <p>처리 절차:
 * <ul>
 *   <li>전체 카테고리 조회 및 가중치 매핑</li>
 *   <li>입력값과 가중치를 바탕으로 점수 계산</li>
 *   <li>계산된 총합 점수를 응답 객체로 포장</li>
 * </ul>
 * @author snowykte0426
 */
@Service
@RequiredArgsConstructor
public class SimulateScoreService implements SimulateScoreUseCase {

    private final CategoryPersistencePort categoryPersistencePort;

    @Override
    public GetScoreSimulateResponse execute(Integer majorAwardCareerOutSchoolOfficial, Integer majorAwardCareerOutSchoolUnofficial, Integer majorAwardCareerOutSchoolHackathon, Integer majorAwardCareerInSchoolGsmfest, Integer majorAwardCareerInSchoolSchoolHackathon, Integer majorAwardCareerInSchoolPresentation, Integer majorCertificateNum, Integer majorTopcitScore, Integer majorClubAttendanceSemester1, Integer majorClubAttendanceSemester2, Integer majorOutSchoolAttendanceOfficialContest, Integer majorOutSchoolAttendanceUnofficialContest, Integer majorOutSchoolAttendanceHackathon, Integer majorOutSchoolAttendanceSeminar, Integer majorInSchoolAttendanceGsmfest, Integer majorInSchoolAttendanceHackathon, Integer majorInSchoolAttendanceClubPresentation, Integer majorInSchoolAttendanceSeminar, Integer majorInSchoolAttendanceAfterSchool, Integer humanitiesAwardCareerHumanityInSchool, Integer humanitiesAwardCareerHumanityOutSchool, Boolean humanitiesReadingReadAThonTurtle, Boolean humanitiesReadingReadAThonCrocodile, Boolean humanitiesReadingReadAThonRabbitOver, Integer humanitiesReading, Integer humanitiesServiceActivity, Integer humanitiesServiceClubSemester1, Integer humanitiesServiceClubSemester2, Boolean humanitiesCertificateChineseCharacter, Boolean humanitiesCertificateKoreanHistory, Integer humanitiesActivitiesSelfDirectedActivities, Integer humanitiesActivitiesNewrrow_S, Boolean foreignLangAttendanceToeicAcademyStatus, Integer foreignLangToeicScore, Integer foreignLangToeflScore, Integer foreignLangTepsScore, Integer foreignLangToeicSpeakingLevel, Integer foreignLangOpicGrade, Integer foreignLangJptScore, Integer foreignLangCptScore, Integer foreignLangHskGrade) {
        List<Category> category = categoryPersistencePort.findAllCategory().stream().map(
                categoryEntity -> Category.builder()
                        .name(SnakeKebabToCamelCaseConverterUtil.toCamelCase(categoryEntity.getName()))
                        .weight(categoryEntity.getWeight())
                        .build()
        ).toList();
        return new GetScoreSimulateResponse(SimulateScoreUtil.simulateScore(
                majorAwardCareerOutSchoolOfficial,
                majorAwardCareerOutSchoolUnofficial,
                majorAwardCareerOutSchoolHackathon,
                majorAwardCareerInSchoolGsmfest,
                majorAwardCareerInSchoolSchoolHackathon,
                majorAwardCareerInSchoolPresentation,
                majorCertificateNum,
                majorTopcitScore,
                majorClubAttendanceSemester1,
                majorClubAttendanceSemester2,
                majorOutSchoolAttendanceOfficialContest,
                majorOutSchoolAttendanceUnofficialContest,
                majorOutSchoolAttendanceHackathon,
                majorOutSchoolAttendanceSeminar,
                majorInSchoolAttendanceGsmfest,
                majorInSchoolAttendanceHackathon,
                majorInSchoolAttendanceClubPresentation,
                majorInSchoolAttendanceSeminar,
                majorInSchoolAttendanceAfterSchool,
                humanitiesAwardCareerHumanityInSchool,
                humanitiesAwardCareerHumanityOutSchool,
                humanitiesReadingReadAThonTurtle,
                humanitiesReadingReadAThonCrocodile,
                humanitiesReadingReadAThonRabbitOver,
                humanitiesReading,
                humanitiesServiceActivity,
                humanitiesServiceClubSemester1,
                humanitiesServiceClubSemester2,
                humanitiesCertificateChineseCharacter,
                humanitiesCertificateKoreanHistory,
                humanitiesActivitiesNewrrow_S,
                humanitiesActivitiesSelfDirectedActivities,
                foreignLangAttendanceToeicAcademyStatus,
                foreignLangToeicScore,
                foreignLangToeflScore,
                foreignLangTepsScore,
                foreignLangToeicSpeakingLevel,
                foreignLangOpicGrade,
                foreignLangJptScore,
                foreignLangCptScore,
                foreignLangHskGrade,
                category.stream().collect(
                        Collectors.toMap(Category::getName, Category::getWeight)
                )
        ).getT4());
    }
}