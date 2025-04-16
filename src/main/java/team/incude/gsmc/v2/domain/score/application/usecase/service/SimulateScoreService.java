package team.incude.gsmc.v2.domain.score.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.usecase.SimulateScoreUseCase;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.presentation.data.response.GetScoreSimulateResponse;
import team.incude.gsmc.v2.global.util.SimulateScoreUtil;
import team.incude.gsmc.v2.global.util.SnakeKebabToCamelCaseConverterUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimulateScoreService implements SimulateScoreUseCase {

    private final CategoryPersistencePort categoryPersistencePort;

    @Override
    public GetScoreSimulateResponse execute(Integer majorAwardCareerOutSchoolOfficial, Integer majorAwardCareerOutSchoolUnofficial, Integer majorAwardCareerOutSchoolHackathon, Integer majorAwardCareerInSchoolGsmfest, Integer majorAwardCareerInSchoolSchoolHackathon, Integer majorAwardCareerInSchoolPresentation, Integer majorCertificateNum, Integer majorTopcitScore, Integer majorClubAttendanceSemester1, Integer majorClubAttendanceSemester2, Integer majorOutSchoolAttendanceOfficialContest, Integer majorOutSchoolAttendanceUnofficialContest, Integer majorOutSchoolAttendanceHackathon, Integer majorOutSchoolAttendanceSeminar, Integer majorInSchoolAttendanceGsmfest, Integer majorInSchoolAttendanceHackathon, Integer majorInSchoolAttendanceClubPresentation, Integer majorInSchoolAttendanceSeminar, Integer majorInSchoolAttendanceAfterSchool, Integer humanitiesAwardCareerHumanityInSchool, Integer humanitiesAwardCareerHumanityOutSchool, Boolean humanitiesReadingReadAThonTurtle, Boolean humanitiesReadingReadAThonCrocodile, Boolean humanitiesReadingReadAThonRabbitOver, Integer humanitiesReading, Integer humanitiesServiceActivity, Integer humanitiesServiceClubSemester1, Integer humanitiesServiceClubSemester2, Integer humanitiesCertificateChineseCharacter, Integer humanitiesCertificateKoreanHistory, Integer humanitiesActivitiesSelfDirectedActivities, Integer humanitiesActivitiesNewrrowS_S, Boolean foreignLangAttendanceToeicAcademyStatus, Integer foreignLangToeicScore, Integer foreignLangToeflScore, Integer foreignLangTepsScore, Integer foreignLangToeicSpeakingLevel, Integer foreignLangOpicGrade, Integer foreignLangJptScore, Integer foreignLangCptScore, Integer foreignLangHskScore) {
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
                humanitiesActivitiesSelfDirectedActivities,
                humanitiesActivitiesNewrrowS_S,
                foreignLangAttendanceToeicAcademyStatus,
                foreignLangToeicScore,
                foreignLangToeflScore,
                foreignLangTepsScore,
                foreignLangToeicSpeakingLevel,
                foreignLangOpicGrade,
                foreignLangJptScore,
                foreignLangCptScore,
                foreignLangHskScore,
                category.stream().collect(
                        Collectors.toMap(Category::getName, Category::getWeight)
                )
        ));
    }
}