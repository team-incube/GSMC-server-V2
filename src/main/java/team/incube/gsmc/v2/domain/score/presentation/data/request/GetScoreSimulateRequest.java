package team.incube.gsmc.v2.domain.score.presentation.data.request;

import jakarta.validation.constraints.NotNull;

/**
 * 모의 점수 계산을 위한 요청 DTO입니다.
 * <p>각 영역별 입력 데이터를 기반으로 점수를 계산하기 위한 시뮬레이션 요청에 사용되며,
 * 전송되는 모든 필드는 필수이며 {@code @NotNull}로 검증됩니다.
 * <p>항목 구성:
 * <ul>
 *   <li>전공 - 수상/자격증/동아리/외부활동 등</li>
 *   <li>인문/인성 - 수상/독서/봉사/자격증/활동</li>
 *   <li>외국어 - 공인시험 점수 및 토익사관학교 수강 여부</li>
 * </ul>
 * <p>본 DTO는 시뮬레이션 결과와 무관하게 단순 점수 계산 요청의 입력 포맷을 정의합니다.
 * @author snowykte0426
 */
public record GetScoreSimulateRequest(
        @NotNull Integer majorAwardCareerOutSchoolOfficial,
        @NotNull Integer majorAwardCareerOutSchoolUnofficial,
        @NotNull Integer majorAwardCareerOutSchoolHackathon,
        @NotNull Integer majorAwardCareerInSchoolGsmfest,
        @NotNull Integer majorAwardCareerInSchoolSchoolHackathon,
        @NotNull Integer majorAwardCareerInSchoolPresentation,
        @NotNull Integer majorCertificateNum,
        @NotNull Integer majorTopcitScore,
        @NotNull Integer majorClubAttendanceSemester1,
        @NotNull Integer majorClubAttendanceSemester2,
        @NotNull Integer majorOutSchoolAttendanceOfficialContest,
        @NotNull Integer majorOutSchoolAttendanceUnofficialContest,
        @NotNull Integer majorOutSchoolAttendanceHackathon,
        @NotNull Integer majorOutSchoolAttendanceSeminar,
        @NotNull Integer majorInSchoolAttendanceGsmfest,
        @NotNull Integer majorInSchoolAttendanceHackathon,
        @NotNull Integer majorInSchoolAttendanceClubPresentation,
        @NotNull Integer majorInSchoolAttendanceSeminar,
        @NotNull Integer majorInSchoolAttendanceAfterSchool,
        @NotNull Integer humanitiesAwardCareerHumanityInSchool,
        @NotNull Integer humanitiesAwardCareerHumanityOutSchool,
        @NotNull Boolean humanitiesReadingReadAThonTurtle,
        @NotNull Boolean humanitiesReadingReadAThonCrocodile,
        @NotNull Boolean humanitiesReadingReadAThonRabbitOver,
        @NotNull Integer humanitiesReading,
        @NotNull Integer humanitiesServiceActivity,
        @NotNull Integer humanitiesServiceClubSemester1,
        @NotNull Integer humanitiesServiceClubSemester2,
        @NotNull Boolean humanitiesCertificateChineseCharacter,
        @NotNull Boolean humanitiesCertificateKoreanHistory,
        @NotNull Integer humanitiesActivitiesNewrrow_S,
        @NotNull Integer humanitiesActivitiesSelfDirectedActivities,
        @NotNull Boolean foreignLangAttendanceToeicAcademyStatus,
        @NotNull Integer foreignLangToeicScore,
        @NotNull Integer foreignLangToeflScore,
        @NotNull Integer foreignLangTepsScore,
        @NotNull Integer foreignLangToeicSpeakingLevel,
        @NotNull Integer foreignLangOpicGrade,
        @NotNull Integer foreignLangJptScore,
        @NotNull Integer foreignLangCptScore,
        @NotNull Integer foreignLangHskGrade
) {
}