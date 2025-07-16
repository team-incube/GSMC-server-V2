package team.incube.gsmc.v2.domain.score.application.port;

import team.incube.gsmc.v2.domain.score.domain.constant.ScoreOrder;
import team.incube.gsmc.v2.domain.score.presentation.data.response.GetScoreResponse;
import team.incube.gsmc.v2.domain.score.presentation.data.response.GetScoreSimulateResponse;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.port.Port;

/**
 * 점수 관련 유스케이스의 진입점을 정의하는 애플리케이션 계층 포트 인터페이스입니다.
 * <p>점수 조회, 수정, 시뮬레이션 및 총합 점수 계산 기능을 제공합니다.
 * 해당 포트는 {@code @Port(direction = PortDirection.INBOUND)}로 선언되어
 * 외부 계층(Web 등)에서 도메인 로직으로의 접근을 허용합니다.
 * <ul>
 *   <li>{@code findCurrentScore} - 현재 사용자 점수 조회</li>
 *   <li>{@code findScoreByStudentCode} - 특정 학생 점수 조회</li>
 *   <li>{@code updateCurrentScore} - 현재 사용자 점수 수정</li>
 *   <li>{@code updateScoreByStudentCode} - 특정 학생 점수 수정</li>
 *   <li>{@code simulateScore} - 입력 데이터를 기반으로 점수 시뮬레이션</li>
 *   <li>{@code calculateTotalScore} - 총합 점수 계산</li>
 * </ul>
 * @author snowykte0426
 */
@Port(direction = PortDirection.INBOUND)
public interface ScoreApplicationPort {
    GetScoreResponse findCurrentScore();

    GetScoreResponse findScoreByEmail(String email);

    void updateCurrentScore(String categoryName, Integer value);

    void updateScoreByEmail(String email, String categoryName, Integer value);

    GetScoreSimulateResponse simulateScore(Integer majorAwardCareerOutSchoolOfficial, Integer majorAwardCareerOutSchoolUnofficial, Integer majorAwardCareerOutSchoolHackathon, Integer majorAwardCareerInSchoolGsmfest, Integer majorAwardCareerInSchoolSchoolHackathon, Integer majorAwardCareerInSchoolPresentation, Integer majorCertificateNum, Integer majorTopcitScore, Integer majorClubAttendanceSemester1, Integer majorClubAttendanceSemester2, Integer majorOutSchoolAttendanceOfficialContest, Integer majorOutSchoolAttendanceUnofficialContest, Integer majorOutSchoolAttendanceHackathon, Integer majorOutSchoolAttendanceSeminar, Integer majorInSchoolAttendanceGsmfest, Integer majorInSchoolAttendanceHackathon, Integer majorInSchoolAttendanceClubPresentation, Integer majorInSchoolAttendanceSeminar, Integer majorInSchoolAttendanceAfterSchool, Integer humanitiesAwardCareerHumanityInSchool, Integer humanitiesAwardCareerHumanityOutSchool, Boolean humanitiesReadingReadAThonTurtle, Boolean humanitiesReadingReadAThonCrocodile, Boolean humanitiesReadingReadAThonRabbitOver, Integer humanitiesReading, Integer humanitiesServiceActivity, Integer humanitiesServiceClubSemester1, Integer humanitiesServiceClubSemester2, Boolean humanitiesCertificateChineseCharacter, Boolean humanitiesCertificateKoreanHistory, Integer humanitiesActivitiesNewrrow_S, Integer humanitiesActivitiesSelfDirectedActivities, Boolean foreignLangAttendanceToeicAcademyStatus, Integer foreignLangToeicScore, Integer foreignLangToeflScore, Integer foreignLangTepsScore, Integer foreignLangToeicSpeakingLevel, Integer foreignLangOpicGrade, Integer foreignLangJptScore, Integer foreignLangCptScore, Integer foreignLangHskGrade);

    void calculateTotalScore(String email);

    Integer getStudentPercentInClass(ScoreOrder scoreOrder, Integer grade, Integer classNumber);

    Integer getStudentPercentInGrade(ScoreOrder scoreOrder, Integer grade);
}