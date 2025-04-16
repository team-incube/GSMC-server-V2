package team.incude.gsmc.v2.domain.score.application.port;

import team.incude.gsmc.v2.domain.score.presentation.data.response.GetScoreResponse;
import team.incude.gsmc.v2.domain.score.presentation.data.response.GetScoreSimulateResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

@Port(direction = PortDirection.INBOUND)
public interface ScoreApplicationPort {
    GetScoreResponse findCurrentScore();

    GetScoreResponse findScoreByEmail(String email);

    void updateCurrentScore(String categoryName, Integer value);

    void updateScoreByEmail(String email, String categoryName, Integer value);

    GetScoreSimulateResponse simulateScore(Integer majorAwardCareerOutSchoolOfficial, Integer majorAwardCareerOutSchoolUnofficial, Integer majorAwardCareerOutSchoolHackathon, Integer majorAwardCareerInSchoolGsmfest, Integer majorAwardCareerInSchoolSchoolHackathon, Integer majorAwardCareerInSchoolPresentation, Integer majorCertificateNum, Integer majorTopcitScore, Integer majorClubAttendanceSemester1, Integer majorClubAttendanceSemester2, Integer majorOutSchoolAttendanceOfficialContest, Integer majorOutSchoolAttendanceUnofficialContest, Integer majorOutSchoolAttendanceHackathon, Integer majorOutSchoolAttendanceSeminar, Integer majorInSchoolAttendanceGsmfest, Integer majorInSchoolAttendanceHackathon, Integer majorInSchoolAttendanceClubPresentation, Integer majorInSchoolAttendanceSeminar, Integer majorInSchoolAttendanceAfterSchool, Integer humanitiesAwardCareerHumanityInSchool, Integer humanitiesAwardCareerHumanityOutSchool, Boolean humanitiesReadingReadAThonTurtle, Boolean humanitiesReadingReadAThonCrocodile, Boolean humanitiesReadingReadAThonRabbitOver, Integer humanitiesReading, Integer humanitiesServiceActivity, Integer humanitiesServiceClubSemester1, Integer humanitiesServiceClubSemester2, Integer humanitiesCertificateChineseCharacter, Integer humanitiesCertificateKoreanHistory, Integer humanitiesActivitiesSelfDirectedActivities, Integer humanitiesActivitiesNewrrowS_S, Boolean foreignLangAttendanceToeicAcademyStatus, Integer foreignLangToeicScore, Integer foreignLangToeflScore, Integer foreignLangTepsScore, Integer foreignLangToeicSpeakingLevel, Integer foreignLangOpicGrade, Integer foreignLangJptScore, Integer foreignLangCptScore, Integer foreignLangHskScore);
}