package team.incude.gsmc.v2.global.util;

import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class SimulateScoreUtil {

    public Integer simulateScore(
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
            Integer humanitiesActivitiesNewrrowS_S,
            Boolean foreignLangAttendanceToeicAcademyStatus,
            Integer foreignLangToeicScore,
            Integer foreignLangToeflScore,
            Integer foreignLangTepsScore,
            Integer foreignLangToeicSpeakingLevel,
            Integer foreignLangOpicGrade,
            Integer foreignLangJptScore,
            Integer foreignLangCptScore,
            Integer foreignLangHskScore,
            Map<String, Float> categoryWeightMap
    ) {
        return majorScore(
                majorAwardCareerOutSchoolOfficial,
                majorAwardCareerOutSchoolUnofficial,
                majorAwardCareerOutSchoolHackathon,
                majorAwardCareerInSchoolGsmfest,
                majorAwardCareerInSchoolSchoolHackathon,
                majorAwardCareerInSchoolPresentation,
                majorCertificateNum,
                majorTopcitScore,
                majorClubAttendanceSemester1 + majorClubAttendanceSemester2,
                majorOutSchoolAttendanceOfficialContest,
                majorOutSchoolAttendanceUnofficialContest,
                majorOutSchoolAttendanceHackathon,
                majorOutSchoolAttendanceSeminar,
                majorInSchoolAttendanceGsmfest,
                majorInSchoolAttendanceHackathon,
                majorInSchoolAttendanceClubPresentation,
                majorInSchoolAttendanceSeminar,
                majorInSchoolAttendanceAfterSchool,
                categoryWeightMap.get("majorAwardCareerOutSchoolOfficial"),
                categoryWeightMap.get("majorCertificateNum"),
                categoryWeightMap.get("majorTopcitScore"),
                categoryWeightMap.get("majorClubAttendanceSemester1"),
                categoryWeightMap.get("majorOutSchoolAttendanceHackathon"),
                categoryWeightMap.get("majorInSchoolAttendanceHackathon"),
                categoryWeightMap.get("majorInSchoolAttendanceSeminar"),
                categoryWeightMap.get("majorInSchoolAttendanceAfterSchool")
        ) + humanitiesScore(
                humanitiesAwardCareerHumanityInSchool,
                humanitiesAwardCareerHumanityOutSchool,
                humanitiesReadingReadAThonTurtle,
                humanitiesReadingReadAThonCrocodile,
                humanitiesReadingReadAThonRabbitOver,
                humanitiesReading,
                humanitiesServiceActivity,
                humanitiesServiceClubSemester1 + humanitiesServiceClubSemester2,
                humanitiesCertificateChineseCharacter,
                humanitiesCertificateKoreanHistory,
                humanitiesActivitiesSelfDirectedActivities,
                humanitiesActivitiesNewrrowS_S,
                categoryWeightMap.get("humanitiesAwardCareerHumanityInSchool"),
                categoryWeightMap.get("humanitiesReadingReadAThonTurtle"),
                categoryWeightMap.get("humanitiesReadingReadAThonCrocodile"),
                categoryWeightMap.get("humanitiesReadingReadAThonRabbitOver"),
                categoryWeightMap.get("humanitiesReading"),
                categoryWeightMap.get("humanitiesServiceActivity"),
                categoryWeightMap.get("humanitiesServiceClubSemester1"),
                categoryWeightMap.get("humanitiesCertificateChineseCharacter"),
                categoryWeightMap.get("humanitiesCertificateKoreanHistory"),
                categoryWeightMap.get("humanitiesActivitiesSelfDirectedActivities"),
                categoryWeightMap.get("humanitiesActivitiesNewrrowS")
        ) + foreignLangScore(
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

    private Integer majorScore(
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
            Float majorAwardCareerWeight,
            Float majorCertificateNumWeight,
            Float majorTopcitScoreWeight,
            Float majorClubAttendanceWeight,
            Float majorOutSchoolAttendanceActivityWeight,
            Float majorInSchoolAttendanceActivityWeight,
            Float majorInSchoolAttendanceSeminarWeight,
            Float majorInSchoolAttendanceAfterSchoolWeight
    ) {
        Float score = 0f;
        Integer majorAwardCareerScore =
                majorAwardCareerOutSchoolOfficial +
                        majorAwardCareerOutSchoolUnofficial +
                        majorAwardCareerOutSchoolHackathon +
                        majorAwardCareerInSchoolGsmfest +
                        majorAwardCareerInSchoolSchoolHackathon +
                        majorAwardCareerInSchoolPresentation;
        score += majorAwardCareerScore > 6 ? 6 * majorAwardCareerWeight : majorAwardCareerScore * majorAwardCareerWeight;
        score += Math.min(majorCertificateNum * majorCertificateNumWeight, 300);
        score += Math.min(majorTopcitScore * majorTopcitScoreWeight, 1000);
        score += majorClubAttendance > 4 ? 4 * majorClubAttendanceWeight : majorClubAttendance * majorClubAttendanceWeight;
        Integer majorOutSchoolAttendanceActivityScore =
                majorOutSchoolAttendanceOfficialContest +
                        majorOutSchoolAttendanceUnofficialContest +
                        majorOutSchoolAttendanceHackathon +
                        majorOutSchoolAttendanceSeminar;
        score += majorOutSchoolAttendanceActivityScore > 8 ? 8 * majorOutSchoolAttendanceActivityWeight : majorOutSchoolAttendanceActivityScore * majorOutSchoolAttendanceActivityWeight;
        score += majorInSchoolAttendanceGsmfest +
                majorInSchoolAttendanceHackathon +
                majorInSchoolAttendanceClubPresentation > 3 ?
                3 * majorInSchoolAttendanceActivityWeight :
                majorInSchoolAttendanceGsmfest +
                        majorInSchoolAttendanceHackathon +
                        majorInSchoolAttendanceClubPresentation * majorInSchoolAttendanceActivityWeight;
        score += majorInSchoolAttendanceSeminar > 4 ? 4 * majorInSchoolAttendanceSeminarWeight : majorInSchoolAttendanceSeminar * majorInSchoolAttendanceSeminarWeight;
        score += majorInSchoolAttendanceAfterSchool > 2 ? 2 * majorInSchoolAttendanceAfterSchoolWeight : majorInSchoolAttendanceAfterSchool * majorInSchoolAttendanceAfterSchoolWeight;
        return score.intValue();
    }

    private Integer humanitiesScore(
            Integer humanitiesAwardCareerHumanityInSchool,
            Integer humanitiesAwardCareerHumanityOutSchool,
            Boolean humanitiesReadingReadAThonTurtle,
            Boolean humanitiesReadingReadAThonCrocodile,
            Boolean humanitiesReadingReadAThonRabbitOver,
            Integer humanitiesReading,
            Integer humanitiesServiceActivity,
            Integer humanitiesServiceClub,
            Integer humanitiesCertificateChineseCharacter,
            Integer humanitiesCertificateKoreanHistory,
            Integer humanitiesActivitiesSelfDirectedActivities,
            Integer humanitiesActivitiesNewrrow_S,
            Float humanitiesAwardCareerWeight,
            Float humanitiesReadingReadAThonTurtleWeight,
            Float humanitiesReadingReadAThonCrocodileWeight,
            Float humanitiesReadingReadAThonRabbitOverWeight,
            Float humanitiesReadingWeight,
            Float humanitiesServiceActivityWeight,
            Float humanitiesServiceClubWeight,
            Float humanitiesCertificateChineseCharacterWeight,
            Float humanitiesCertificateKoreanHistoryWeight,
            Float humanitiesActivitiesSelfDirectedActivitiesWeight,
            Float humanitiesActivitiesNewrrow_SWeight
    ) {
        Float score = 0f;
        Integer humanitiesAwardCareerScore =
                humanitiesAwardCareerHumanityInSchool +
                        humanitiesAwardCareerHumanityOutSchool;
        score += humanitiesAwardCareerScore > 4 ? 4 * humanitiesAwardCareerWeight : humanitiesAwardCareerScore * humanitiesAwardCareerWeight;
        score += humanitiesReadingReadAThonRabbitOver ? humanitiesReadingReadAThonRabbitOverWeight :
                humanitiesReadingReadAThonCrocodile ? humanitiesReadingReadAThonCrocodileWeight :
                        humanitiesReadingReadAThonTurtle ? humanitiesReadingReadAThonTurtleWeight : 0;
        score += humanitiesReading > 10 ? 10 * humanitiesReadingWeight : humanitiesReading * humanitiesReadingWeight;
        score += humanitiesServiceActivity > 40 ? 40 * humanitiesServiceActivityWeight : humanitiesServiceActivity * humanitiesServiceActivityWeight;
        score += humanitiesServiceClub > 2 ? 2 * humanitiesServiceClubWeight : humanitiesServiceClub * humanitiesServiceClubWeight;
        score += humanitiesCertificateChineseCharacter > 0 ? humanitiesCertificateChineseCharacterWeight : 0;
        score += humanitiesCertificateKoreanHistory > 0 ? humanitiesCertificateKoreanHistoryWeight : 0;
        score += Math.min(humanitiesActivitiesNewrrow_S * humanitiesActivitiesNewrrow_SWeight, 200);
        score += humanitiesActivitiesSelfDirectedActivities > 8 ? 8 * humanitiesActivitiesSelfDirectedActivitiesWeight : humanitiesActivitiesSelfDirectedActivities * humanitiesActivitiesSelfDirectedActivitiesWeight;
        return score.intValue();
    }

    private Integer foreignLangScore(
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
        Integer score = 0;
        score += foreignLangAttendanceToeicAcademyStatus ? 100 : 0;
        score += foreignLangToeicScore >= 700 ? 500 :
                foreignLangToeicScore >= 630 ? 450 :
                        foreignLangToeicScore >= 560 ? 400 :
                                foreignLangToeicScore >= 490 ? 350 :
                                        foreignLangToeicScore >= 420 ? 300 :
                                                foreignLangToeicScore >= 300 ? 250 : 0;
        score += foreignLangToeflScore >= 80 ? 500 :
                foreignLangToeflScore >= 75 ? 450 :
                        foreignLangToeflScore >= 70 ? 400 :
                                foreignLangToeflScore >= 65 ? 350 :
                                        foreignLangToeflScore >= 60 ? 300 :
                                                foreignLangToeflScore >= 55 ? 250 : 0;
        score += foreignLangTepsScore >= 555 ? 500 :
                foreignLangTepsScore >= 520 ? 450 :
                        foreignLangTepsScore >= 480 ? 400 :
                                foreignLangTepsScore >= 440 ? 350 :
                                        foreignLangTepsScore >= 400 ? 300 :
                                                foreignLangTepsScore >= 360 ? 250 : 0;
        score += foreignLangToeicSpeakingLevel >= 6 ? 500 :
                foreignLangToeicSpeakingLevel >= 5 ? 450 :
                        foreignLangToeicSpeakingLevel >= 4 ? 400 :
                                foreignLangToeicSpeakingLevel >= 3 ? 350 : 0;
        score += foreignLangOpicGrade >= 4 ? 500 :
                foreignLangOpicGrade >= 3 ? 450 :
                        foreignLangOpicGrade >= 2 ? 400 :
                                foreignLangOpicGrade >= 1 ? 350 : 0;
        score += foreignLangJptScore >= 700 ? 500 :
                foreignLangJptScore >= 630 ? 450 :
                        foreignLangJptScore >= 560 ? 400 :
                                foreignLangJptScore >= 490 ? 350 :
                                        foreignLangJptScore >= 420 ? 300 :
                                                foreignLangJptScore >= 350 ? 250 : 0;
        score += foreignLangCptScore >= 651 ? 500 :
                foreignLangCptScore >= 501 ? 450 :
                        foreignLangCptScore >= 351 ? 400 :
                                foreignLangCptScore >= 201 ? 350 : 0;
        score += foreignLangHskScore >= 6 ? 500 :
                foreignLangHskScore >= 5 ? 450 :
                        foreignLangHskScore >= 4 ? 400 :
                                foreignLangHskScore >= 3 ? 350 :
                                        foreignLangHskScore >= 2 ? 300 : 0;
        return score;
    }
}