package team.incube.gsmc.v2.domain.score.application;

import lombok.RequiredArgsConstructor;
import team.incube.gsmc.v2.domain.score.application.port.ScoreApplicationPort;
import team.incube.gsmc.v2.domain.score.application.usecase.*;
import team.incube.gsmc.v2.domain.score.domain.constant.PercentileType;
import team.incube.gsmc.v2.domain.score.presentation.data.response.GetScoreResponse;
import team.incube.gsmc.v2.domain.score.presentation.data.response.GetScoreSimulateResponse;
import team.incube.gsmc.v2.domain.score.presentation.data.response.GetStudentPercentResponse;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;

/**
 * 점수 관련 유스케이스를 실행하는 애플리케이션 어댑터 클래스입니다.
 * <p>{@link ScoreApplicationPort}를 구현하며, 점수 조회, 수정, 시뮬레이션 및 총합 계산 기능을 어댑터 계층에 제공합니다.
 * 각 기능은 해당 유스케이스 인터페이스를 통해 실제 비즈니스 로직에 위임됩니다.
 * <p>주요 책임:
 * <ul>
 *   <li>현재 사용자 및 특정 학생의 점수 조회</li>
 *   <li>점수 수정 (현재 사용자 및 특정 학생)</li>
 *   <li>모의 점수 계산 (입력값 기반)</li>
 *   <li>총합 점수 계산 요청</li>
 * </ul>
 * @author snowykte0426
 */
@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class ScoreApplicationAdapter implements ScoreApplicationPort {

    private final FindScoreUseCase findScoreUseCase;
    private final UpdateScoreUseCase updateScoreUseCase;
    private final SimulateScoreUseCase simulateScoreUseCase;
    private final CalculateTotalScoreUseCase calculateTotalScoreUseCase;
    private final GetStudentPercentInClassUseCase getStudentPercentInClassUseCase;
    private final GetStudentPercentInGradeUseCase getStudentPercentInGradeUseCase;

    /**
     * 현재 로그인한 사용자의 점수를 조회합니다.
     * @return 점수 응답 DTO
     */
    @Override
    public GetScoreResponse findCurrentScore() {
        return findScoreUseCase.execute();
    }

    /**
     * 특정 학생 코드에 해당하는 사용자의 점수를 조회합니다.
     * @param email 학생 이메일
     * @return 점수 응답 DTO
     */
    @Override
    public GetScoreResponse findScoreByEmail(String email) {
        return findScoreUseCase.execute(email);
    }

    /**
     * 현재 로그인한 사용자의 특정 카테고리 점수를 수정합니다.
     *
     * @param categoryName 수정할 카테고리 이름
     * @param value 수정할 점수 값
     */
    @Override
    public void updateCurrentScore(String categoryName, Integer value) {
        updateScoreUseCase.execute(categoryName, value);
    }

    /**
     * 특정 학생의 특정 카테고리 점수를 수정합니다.
     * @param email 학생 이메일
     * @param categoryName 수정할 카테고리 이름
     * @param value 수정할 점수 값
     */
    @Override
    public void updateScoreByEmail(String email, String categoryName, Integer value) {
        updateScoreUseCase.execute(email, categoryName, value);
    }

    /**
     * 입력된 데이터를 기반으로 모의 점수를 계산합니다.
     * @return 시뮬레이션 점수 응답 DTO
     */
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
            Boolean humanitiesCertificateChineseCharacter,
            Boolean humanitiesCertificateKoreanHistory,
            Integer humanitiesActivitiesNewrrow_S,
            Integer humanitiesActivitiesSelfDirectedActivities,
            Boolean foreignLangAttendanceToeicAcademyStatus,
            Integer foreignLangToeicScore,
            Integer foreignLangToeflScore,
            Integer foreignLangTepsScore,
            Integer foreignLangToeicSpeakingLevel,
            Integer foreignLangOpicGrade,
            Integer foreignLangJptScore,
            Integer foreignLangCptScore,
            Integer foreignLangHskGrade
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
                foreignLangHskGrade
        );
    }

    /**
     * 특정 학생의 총합 점수를 계산합니다.
     * @param studentCode 학생 고유 코드
     */
    @Override
    public void calculateTotalScore(String studentCode) {
        calculateTotalScoreUseCase.execute(studentCode);
    }

    @Override
    public GetStudentPercentResponse getStudentPercentInClass(PercentileType percentileType, Integer grade, Integer classNumber) {
        return getStudentPercentInClassUseCase.execute(percentileType, grade, classNumber);
    }

    @Override
    public GetStudentPercentResponse getStudentPercentInGrade(PercentileType percentileType, Integer grade) {
        return getStudentPercentInGradeUseCase.execute(percentileType, grade);
    }
}