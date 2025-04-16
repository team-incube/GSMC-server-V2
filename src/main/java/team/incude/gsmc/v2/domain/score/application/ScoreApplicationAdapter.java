package team.incude.gsmc.v2.domain.score.application;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.score.application.port.ScoreApplicationPort;
import team.incude.gsmc.v2.domain.score.application.usecase.FindScoreUseCase;
import team.incude.gsmc.v2.domain.score.application.usecase.SimulateScoreUseCase;
import team.incude.gsmc.v2.domain.score.application.usecase.UpdateScoreUseCase;
import team.incude.gsmc.v2.domain.score.presentation.data.request.GetScoreSimulateRequest;
import team.incude.gsmc.v2.domain.score.presentation.data.response.GetScoreResponse;
import team.incude.gsmc.v2.domain.score.presentation.data.response.GetScoreSimulateResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class ScoreApplicationAdapter implements ScoreApplicationPort {

    private final FindScoreUseCase findScoreUseCase;
    private final UpdateScoreUseCase updateScoreUseCase;
    private final SimulateScoreUseCase simulateScoreUseCase;

    @Override
    public GetScoreResponse findCurrentScore() {
        return findScoreUseCase.execute();
    }

    @Override
    public GetScoreResponse findScoreByEmail(String email) {
        return findScoreUseCase.execute(email);
    }

    @Override
    public void updateCurrentScore(String categoryName, Integer value) {
        updateScoreUseCase.execute(categoryName, value);
    }

    @Override
    public void updateScoreByEmail(String email, String categoryName, Integer value) {
        updateScoreUseCase.execute(email, categoryName, value);
    }

    @Override
    public GetScoreSimulateResponse simulateScore(
            Integer majorAwardCareerOutSchoolOfficial,
            Integer majorAwardCareerOutSchoolUnofficial,
            Integer majorAwardCareerOutSchoolHackathon,
            Integer majorAwardCareerInSchoolGsmfest,
            Integer majorAwardCareerInSchoolSchoolHackathon,
            Integer majorAwardCareerInSchoolPresentation,
            Integer majorCertificateNum,
            Integer majorTopcitScore,
            Integer majorClubAttendanceSemester1,
            Integer majorClubAttendanceSemester2,
            Integer majorOutSchoolAttendanceOfficialContest,
            Integer majorOutSchoolAttendanceUnofficialContest,
            Integer majorOutSchoolAttendanceHackathon,
            Integer majorOutSchoolAttendanceSeminar,
            Integer majorInSchoolAttendanceGsmfest,
            Integer majorInSchoolAttendanceHackathon,
            Integer majorInSchoolAttendanceClubPresentation,
            Integer majorInSchoolAttendanceSeminar,
            Integer majorInSchoolAttendanceAfterSchool,
            Integer humanitiesAwardCareerHumanityInSchool,
            Integer humanitiesAwardCareerHumanityOutSchool,
            Boolean humanitiesReadingReadAThonTurtle,
            Boolean humanitiesReadingReadAThonCrocodile,
            Boolean humanitiesReadingReadAThonRabbitOver,
            Integer humanitiesReading,
            Integer humanitiesServiceActivity,
            Integer humanitiesServiceClubSemester1,
            Integer humanitiesServiceClubSemester2,
            Integer humanitiesCertificateChineseCharacter,
            Integer humanitiesCertificateKoreanHistory,
            Integer humanitiesActivitiesSelfDirectedActivities,
            Integer humanitiesActivitiesNewrrow_S,
            Boolean foreignLangAttendanceToeicAcademyStatus,
            Integer foreignLangToeicScore,
            Integer foreignLangToeflScore,
            Integer foreignLangTepsScore,
            Integer foreignLangToeicSpeakingLevel,
            Integer foreignLangOpicGrade,
            Integer foreignLangJptScore,
            Integer foreignLangCptScore,
            Integer foreignLangHskScore
    ) {
        return simulateScoreUseCase.execute(
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
                humanitiesActivitiesNewrrow_S,
                foreignLangAttendanceToeicAcademyStatus,
                foreignLangToeicScore,
                foreignLangToeflScore,
                foreignLangTepsScore,
                foreignLangToeicSpeakingLevel,
                foreignLangOpicGrade,
                foreignLangJptScore,
                foreignLangCptScore,
                foreignLangHskScore
        );
    }
}