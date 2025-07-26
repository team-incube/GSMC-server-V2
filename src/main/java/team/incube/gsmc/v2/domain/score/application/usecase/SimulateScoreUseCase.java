package team.incube.gsmc.v2.domain.score.application.usecase;

import team.incube.gsmc.v2.domain.score.presentation.data.response.GetScoreSimulateResponse;

/**
 * 모의 점수 계산 유스케이스를 정의하는 인터페이스입니다.
 * <p>사용자가 입력한 다양한 항목(전공, 인문, 외국어 영역 등)에 대해 점수를 계산하여 시뮬레이션 결과를 제공합니다.
 * {@link GetScoreSimulateResponse}를 반환하며, UI에서 입력된 데이터를 기반으로 점수 예측 기능에 사용됩니다.
 * <p>예측 대상 항목은 다음과 같이 구성됩니다:
 * <ul>
 *   <li>전공 영역: 수상, 자격증, 동아리, 외부활동</li>
 *   <li>인문 영역: 수상, 독서, 봉사, 활동</li>
 *   <li>외국어 영역: 공인시험 성적, 어학원 수강 여부</li>
 * </ul>
 * @author snowykte0426
 */
public interface SimulateScoreUseCase {
    GetScoreSimulateResponse execute(Integer majorAwardCareerOutSchoolOfficial, Integer majorAwardCareerOutSchoolUnofficial, Integer majorAwardCareerOutSchoolHackathon, Integer majorAwardCareerInSchoolGsmfest, Integer majorAwardCareerInSchoolSchoolHackathon, Integer majorAwardCareerInSchoolPresentation, Integer majorCertificateNum, Integer majorTopcitScore, Integer majorClubAttendanceSemester1, Integer majorClubAttendanceSemester2, Integer majorOutSchoolAttendanceOfficialContest, Integer majorOutSchoolAttendanceUnofficialContest, Integer majorOutSchoolAttendanceHackathon, Integer majorOutSchoolAttendanceSeminar, Integer majorInSchoolAttendanceGsmfest, Integer majorInSchoolAttendanceHackathon, Integer majorInSchoolAttendanceClubPresentation, Integer majorInSchoolAttendanceSeminar, Integer majorInSchoolAttendanceAfterSchool, Integer humanitiesAwardCareerHumanityInSchool, Integer humanitiesAwardCareerHumanityOutSchool, Boolean humanitiesReadingReadAThonTurtle, Boolean humanitiesReadingReadAThonCrocodile, Boolean humanitiesReadingReadAThonRabbitOver, Integer humanitiesReading, Integer humanitiesServiceActivity, Integer humanitiesServiceClubSemester1, Integer humanitiesServiceClubSemester2, Boolean humanitiesCertificateChineseCharacter, Boolean humanitiesCertificateKoreanHistory, Integer humanitiesActivitiesSelfDirectedActivities, Integer humanitiesActivitiesNewrrow_S, Boolean foreignLangAttendanceToeicAcademyStatus, Integer foreignLangToeicScore, Integer foreignLangToeflScore, Integer foreignLangTepsScore, Integer foreignLangToeicSpeakingLevel, Integer foreignLangOpicGrade, Integer foreignLangJptScore, Integer foreignLangCptScore, Integer foreignLangHskGrade);
}