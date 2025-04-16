package team.incude.gsmc.v2.domain.score.presentation.data.request;

public record GetScoreSimulateRequest(
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
        Integer humanitiesServiceActivity,
        Integer humanitiesServiceClubSemester1,
        Integer humanitiesServiceClubSemester2,
        Integer humanitiesCertificateChineseCharacter,
        Integer humanitiesCertificateKoreanHistory,
        Integer humanitiesActivitiesSelfDirectedActivities,
        Integer humanitiesActivitiesNewrrowS_S,
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
}