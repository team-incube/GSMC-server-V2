package team.incube.gsmc.v2.global.util;

import lombok.experimental.UtilityClass;
import team.incube.gsmc.v2.domain.score.exception.CategoryNotFoundException;

/**
 * 특정 카테고리명과 값을 기반으로 개별 점수를 계산하는 유틸리티 클래스입니다.
 * <p>{@link SimulateScoreUtil}과 동일한 계산 로직을 사용하여 개별 카테고리별 점수를 계산합니다.
 * 가중치를 적용한 점수 계산과 외국어 영역의 임계값 기반 점수 매핑을 지원합니다.
 * <p>클라이언트에서 특정 활동의 점수 기여도를 실시간으로 확인하거나, 개별 카테고리별 점수 분석에 활용됩니다.
 * {@code @UtilityClass}로 선언되어 인스턴스화가 불가능하며, 모든 메서드는 정적(static)으로 제공됩니다.
 * @author snowykte0426
 */
@UtilityClass
public class ScoreCalculatorUtil {

    /**
     * 카테고리명과 값, 가중치를 모두 고려하여 점수를 계산합니다.
     * <p>{@link SimulateScoreUtil}과 동일한 계산 로직을 사용합니다.
     * @param categoryName 카테고리 이름
     * @param value 해당 카테고리의 값 (Integer 또는 Boolean)
     * @param weight 가중치 (외국어 영역은 가중치 무시)
     * @return 계산된 점수
     * @throws IllegalArgumentException 지원하지 않는 카테고리이거나 잘못된 값 타입인 경우
     */
    public int calculateScore(String categoryName, Object value, Float weight) {
        if (value == null) {
            return 0;
        }

        boolean isBooleanCategory = categoryName.equals("humanitiesReadingReadAThonTurtle") ||
                                  categoryName.equals("humanitiesReadingReadAThonCrocodile") ||
                                  categoryName.equals("humanitiesReadingReadAThonRabbitOver") ||
                                  categoryName.equals("humanitiesCertificateChineseCharacter") ||
                                  categoryName.equals("humanitiesCertificateKoreanHistory");
        
        if (isBooleanCategory && value instanceof Integer) {
            value = !value.equals(0);
        }

        return switch (categoryName) {
            case "majorAwardCareerOutSchoolOfficial",
                 "majorAwardCareerOutSchoolUnofficial",
                 "majorAwardCareerOutSchoolHackathon",
                 "majorAwardCareerInSchoolGsmfest",
                 "majorAwardCareerInSchoolSchoolHackathon",
                 "majorAwardCareerInSchoolPresentation" -> calculateMajorAwardScore((Integer) value, weight);

            case "majorCertificateNum" -> calculateCertificateScore((Integer) value, weight);
            case "majorTopcitScore" -> calculateTopcitScore((Integer) value, weight);

            case "majorClubAttendanceSemester1",
                 "majorClubAttendanceSemester2" -> calculateClubAttendanceScore((Integer) value, weight);

            case "majorOutSchoolAttendanceOfficialContest",
                 "majorOutSchoolAttendanceUnofficialContest",
                 "majorOutSchoolAttendanceHackathon",
                 "majorOutSchoolAttendanceSeminar" -> calculateOutSchoolActivityScore((Integer) value, weight);

            case "majorInSchoolAttendanceGsmfest",
                 "majorInSchoolAttendanceHackathon",
                 "majorInSchoolAttendanceClubPresentation" -> calculateInSchoolActivityScore((Integer) value, weight);

            case "majorInSchoolAttendanceSeminar" -> calculateInSchoolSeminarScore((Integer) value, weight);
            case "majorInSchoolAttendanceAfterSchool" -> calculateAfterSchoolScore((Integer) value, weight);

            case "humanitiesAwardCareerHumanityInSchool",
                 "humanitiesAwardCareerHumanityOutSchool" -> calculateHumanitiesAwardScore((Integer) value, weight);

            case "humanitiesReadingReadAThonTurtle", "humanitiesReadingReadAThonCrocodile",
                 "humanitiesReadingReadAThonRabbitOver", "humanitiesCertificateChineseCharacter",
                 "humanitiesCertificateKoreanHistory" -> (Boolean) value ? Math.round(weight) : 0;
            case "humanitiesReading" -> calculateReadingScore((Integer) value, weight);

            case "humanitiesServiceActivity" -> calculateServiceActivityScore((Integer) value, weight);
            case "humanitiesServiceClubSemester1",
                 "humanitiesServiceClubSemester2" -> calculateServiceClubScore((Integer) value, weight);

            case "humanitiesActivitiesNewrrowS" -> calculateNewrrowSScore((Integer) value, weight);
            case "humanitiesActivitiesSelfDirectedActivities" -> calculateSelfDirectedScore((Integer) value, weight);

            case "foreignLangToeicScore" -> calculateToeicScore((Integer) value, false);
            case "foreignLangToeflScore" -> calculateToeflScore((Integer) value);
            case "foreignLangTepsScore" -> calculateTepsScore((Integer) value);
            case "foreignLangToeicSpeakingLevel" -> calculateToeicSpeakingScore((Integer) value);
            case "foreignLangOpicGrade" -> calculateOpicGrade((Integer) value);
            case "foreignLangJptScore" -> calculateJptScore((Integer) value);
            case "foreignLangCptScore" -> calculateCptScore((Integer) value);
            case "foreignLangHskGrade" -> calculateHskGrade((Integer) value);

            // 특별 처리 필요
            case "foreignLangAttendanceToeicAcademyStatus" -> 0; // TOEIC 점수와 함께 계산되어야 함

            default -> throw new CategoryNotFoundException();
        };
    }

    /**
     * TOEIC 점수와 학원 출석 상태를 함께 고려하여 점수를 계산합니다.
     *
     * @param toeicScore TOEIC 점수
     * @param hasAcademyAttendance 학원 출석 여부
     * @return 계산된 점수
     */
    public int calculateToeicWithAcademy(Integer toeicScore, Boolean hasAcademyAttendance) {
        if (toeicScore == null) {
            return 0;
        }
        return calculateToeicScore(toeicScore, Boolean.TRUE.equals(hasAcademyAttendance));
    }

    /**
     * 전공 수상 경력 점수를 계산합니다.
     * 주의: SimulateScoreUtil에서는 전체 수상 개수에 대해 상한 6개가 적용됩니다.
     * 개별 카테고리 계산 시에는 해당 값에 가중치만 적용합니다.
     */
    private int calculateMajorAwardScore(Integer value, Float weight) {
        if (value == null || weight == null) {
            return 0;
        }
        return Math.round(value * weight);
    }

    private int calculateCertificateScore(Integer count, Float weight) {
        if (count == null || weight == null) {
            return 0;
        }
        return Math.min(Math.round(count * weight), 300);
    }

    private int calculateTopcitScore(Integer score, Float weight) {
        if (score == null || weight == null) {
            return 0;
        }
        return Math.min(Math.round(score * weight), 1000);
    }

    /**
     * 동아리 출석 점수를 계산합니다.
     * 주의: SimulateScoreUtil에서는 학기별 합산 후 상한 4개가 적용됩니다.
     */
    private int calculateClubAttendanceScore(Integer count, Float weight) {
        if (count == null || weight == null) {
            return 0;
        }
        return Math.round(count * weight);
    }

    private int calculateOutSchoolActivityScore(Integer count, Float weight) {
        if (count == null || weight == null) {
            return 0;
        }
        return Math.round(count * weight);
    }

    private int calculateInSchoolActivityScore(Integer count, Float weight) {
        if (count == null || weight == null) {
            return 0;
        }
        return Math.round(count * weight);
    }

    private int calculateInSchoolSeminarScore(Integer count, Float weight) {
        if (count == null || weight == null) {
            return 0;
        }
        return Math.round(Math.min(count, 4) * weight);
    }

    private int calculateAfterSchoolScore(Integer count, Float weight) {
        if (count == null || weight == null) {
            return 0;
        }
        return Math.round(Math.min(count, 2) * weight);
    }

    /**
     * 인문 수상 경력 점수를 계산합니다.
     * 주의: SimulateScoreUtil에서는 전체 인문 수상 개수에 대해 상한 4개가 적용됩니다.
     */
    private int calculateHumanitiesAwardScore(Integer count, Float weight) {
        if (count == null || weight == null) {
            return 0;
        }
        return Math.round(count * weight);
    }

    private int calculateReadingScore(Integer count, Float weight) {
        if (count == null || weight == null) {
            return 0;
        }
        return Math.round(Math.min(count, 10) * weight);
    }

    private int calculateServiceActivityScore(Integer hours, Float weight) {
        if (hours == null || weight == null) {
            return 0;
        }
        return Math.round(Math.min(hours, 40) * weight);
    }

    /**
     * 봉사동아리 점수를 계산합니다.
     * 주의: SimulateScoreUtil에서는 학기별 합산 후 상한 2개가 적용됩니다.
     */
    private int calculateServiceClubScore(Integer count, Float weight) {
        if (count == null || weight == null) {
            return 0;
        }
        return Math.round(count * weight);
    }

    private int calculateNewrrowSScore(Integer count, Float weight) {
        if (count == null || weight == null) {
            return 0;
        }
        return Math.min(Math.round(count * weight), 200);
    }

    private int calculateSelfDirectedScore(Integer count, Float weight) {
        if (count == null || weight == null) {
            return 0;
        }
        return Math.round(Math.min(count, 8) * weight);
    }

    private int calculateToeicScore(Integer score, boolean hasAcademy) {
        if (score == null) {
            return 0;
        }
        int adjustedScore = score + (hasAcademy ? 100 : 0);
        int[] cutoffs = {700, 630, 560, 490, 420, 300};
        int[] scores = {500, 450, 400, 350, 300, 250};
        return mapScoreByThreshold(adjustedScore, cutoffs, scores);
    }

    private int calculateToeflScore(Integer score) {
        if (score == null) {
            return 0;
        }
        int[] cutoffs = {80, 75, 70, 65, 60, 55};
        int[] scores = {500, 450, 400, 350, 300, 250};
        return mapScoreByThreshold(score, cutoffs, scores);
    }

    private int calculateTepsScore(Integer score) {
        if (score == null) {
            return 0;
        }
        int[] cutoffs = {555, 520, 480, 440, 400, 360};
        int[] scores = {500, 450, 400, 350, 300, 250};
        return mapScoreByThreshold(score, cutoffs, scores);
    }

    private int calculateToeicSpeakingScore(Integer level) {
        if (level == null) {
            return 0;
        }
        int[] cutoffs = {6, 5, 4, 3};
        int[] scores = {500, 450, 400, 350};
        return mapScoreByThreshold(level, cutoffs, scores);
    }

    private int calculateOpicGrade(Integer grade) {
        if (grade == null) {
            return 0;
        }
        int[] cutoffs = {4, 3, 2, 1};
        int[] scores = {500, 450, 400, 350};
        return mapScoreByThreshold(grade, cutoffs, scores);
    }

    private int calculateJptScore(Integer score) {
        if (score == null) {
            return 0;
        }
        int[] cutoffs = {700, 630, 560, 490, 420, 350};
        int[] scores = {500, 450, 400, 350, 300, 250};
        return mapScoreByThreshold(score, cutoffs, scores);
    }

    private int calculateCptScore(Integer score) {
        if (score == null) {
            return 0;
        }
        int[] cutoffs = {651, 501, 351, 201};
        int[] scores = {500, 450, 400, 350};
        return mapScoreByThreshold(score, cutoffs, scores);
    }

    private int calculateHskGrade(Integer level) {
        if (level == null) {
            return 0;
        }
        int[] cutoffs = {6, 5, 4, 3, 2};
        int[] scores = {500, 450, 400, 350, 300};
        return mapScoreByThreshold(level, cutoffs, scores);
    }

    /**
     * 임계값 기반으로 점수를 매핑합니다.
     * {@link SimulateScoreUtil#mapThreshold(int, int[], int[])}와 동일한 로직입니다.
     * @param value 입력 값
     * @param cutoffs 임계값 배열 (내림차순 정렬)
     * @param scores 해당하는 점수 배열
     * @return 매핑된 점수
     */
    private int mapScoreByThreshold(int value, int[] cutoffs, int[] scores) {
        for (int i = 0; i < cutoffs.length; i++) {
            if (value >= cutoffs[i]) {
                return scores[i];
            }
        }
        return 0;
    }
}