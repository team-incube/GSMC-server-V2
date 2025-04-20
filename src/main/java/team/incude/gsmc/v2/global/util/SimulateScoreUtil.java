package team.incude.gsmc.v2.global.util;

import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class SimulateScoreUtil {

    private static final Integer MAX_FOREIGN_LANG_SCORE = 500;

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
            Boolean humanitiesCertificateChineseCharacter,
            Boolean humanitiesCertificateKoreanHistory,
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
        int major    = majorScore(
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
                categoryWeightMap
        );

        int humanity = humanitiesScore(
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
                categoryWeightMap
        );

        int foreignLang = foreignLangScore(
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

        return major + humanity + foreignLang;
    }

    private int majorScore(
            Integer outOffi, Integer outUnoff, Integer outHack,
            Integer inFest, Integer inHack, Integer inPres,
            Integer certNum, Integer topcit,
            Integer clubAttend,
            Integer outOffiCon, Integer outUnoffCon, Integer outHackCon, Integer outSeminar,
            Integer inFestCon, Integer inHackCon, Integer inClubPres, Integer inSeminar, Integer inAfterSchool,
            Map<String, Float> w
    ) {
        float score = 0f;

        int awardCount = outOffi + outUnoff + outHack + inFest + inHack + inPres;
        score += capped(awardCount, 6) * w.get("majorAwardCareerOutSchoolOfficial");

        score += Math.min(certNum * w.get("majorCertificateNum"), 300);
        score += Math.min(topcit * w.get("majorTopcitScore"), 1000);

        score += capped(clubAttend, 4) * w.get("majorClubAttendanceSemester1");

        int outActivities = outOffiCon + outUnoffCon + outHackCon + outSeminar;
        score += capped(outActivities, 8) * w.get("majorOutSchoolAttendanceHackathon");

        int inActivities = inFestCon + inHackCon + inClubPres;
        score += capped(inActivities, 3) * w.get("majorInSchoolAttendanceHackathon");

        score += capped(inSeminar, 4) * w.get("majorInSchoolAttendanceSeminar");
        score += capped(inAfterSchool, 2) * w.get("majorInSchoolAttendanceAfterSchool");

        return Math.round(score);
    }

    private int humanitiesScore(
            Integer awardIn, Integer awardOut,
            Boolean turtle, Boolean croc, Boolean rabbit,
            Integer reading, Integer serviceAct, Integer serviceClub,
            Boolean hanja, Boolean history,
            Integer selfDir, Integer afterSchool,
            Map<String, Float> w
    ) {
        float score = 0f;

        int awardCount = awardIn + awardOut;
        score += capped(awardCount, 4) * w.get("humanitiesAwardCareerHumanityInSchool");
        if (rabbit) {
            score += w.get("humanitiesReadingReadAThonRabbitOver");
        } else if (croc) {
            score += w.get("humanitiesReadingReadAThonCrocodile");
        } else if (turtle) {
            score += w.get("humanitiesReadingReadAThonTurtle");
        }
        score += capped(reading, 10) * w.get("humanitiesReading");
        score += capped(serviceAct, 40) * w.get("humanitiesServiceActivity");
        score += capped(serviceClub, 2) * w.get("humanitiesServiceClubSemester1");
        score += hanja   ? w.get("humanitiesCertificateChineseCharacter") : 0;
        score += history ? w.get("humanitiesCertificateKoreanHistory") : 0;
        score += Math.min(afterSchool * w.get("humanitiesActivitiesNewrrowS"), 200);
        score += capped(selfDir, 8) * w.get("humanitiesActivitiesSelfDirectedActivities");
        return Math.round(score);
    }

    private int foreignLangScore(
            Boolean academy, Integer toeic, Integer toefl, Integer teps,
            Integer speaking, Integer opic, Integer jpt, Integer cpt, Integer hsk
    ) {
        int score = 0;
        int[][] cutoffs = {
                {700, 630, 560, 490, 420, 300},
                { 80,  75,  70,  65,  60,  55},
                {555, 520, 480, 440, 400, 360},
                {  6,   5,   4,   3},
                {  4,   3,   2,   1},
                {700, 630, 560, 490, 420, 350},
                {651, 501, 351, 201},
                {  6,   5,   4,   3,   2}
        };
        int[][] scores = {
                {500, 450, 400, 350, 300, 250},
                {500, 450, 400, 350, 300, 250},
                {500, 450, 400, 350, 300, 250},
                {500, 450, 400, 350},
                {500, 450, 400, 350},
                {500, 450, 400, 350, 300, 250},
                {500, 450, 400, 350},
                {500, 450, 400, 350, 300}
        };
        int[] values = {
                toeic + (academy ? 100 : 0),
                toefl, teps, speaking, opic, jpt, cpt, hsk
        };
        for (int i = 0; i < values.length; i++) {
            score += mapThreshold(values[i], cutoffs[i], scores[i]);
            if (score >= MAX_FOREIGN_LANG_SCORE) {
                return MAX_FOREIGN_LANG_SCORE;
            }
        }
        return score;
    }

    private int capped(int n, int cap) {
        return Math.min(n, cap);
    }

    private int mapThreshold(int value, int[] cutoffs, int[] scores) {
        for (int i = 0; i < cutoffs.length; i++) {
            if (value >= cutoffs[i]) {
                return scores[i];
            }
        }
        return 0;
    }
}