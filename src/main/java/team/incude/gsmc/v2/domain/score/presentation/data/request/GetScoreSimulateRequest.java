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
        Integer majorClubAttendance,
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
        String humanitiesBookBookmarathonTurtle,
        Integer humanitiesBookBookmarathon,
        Integer humanitiesServiceActivity,
        Integer humanitiesServiceClub,
        Integer humanitiesCertificateChineseCharacter,
        Integer humanitiesCertificateKoreanHistory,
        Integer humanitiesActivitiesSelfDirectedActivities,
        Integer humanitiesActivitiesNewrrowS_S,
        Boolean foreignLangAttendanceToeicAcademyStatus,
        String foreignLangTest,
        Integer foreignLangTestScore
        //TODO: 외국어 점수 과목 세분화 필요
) {
}