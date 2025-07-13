package team.incube.gsmc.v2.domain.score.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.incube.gsmc.v2.domain.score.application.port.ScoreApplicationPort;
import team.incube.gsmc.v2.domain.score.presentation.data.request.GetScoreSimulateRequest;
import team.incube.gsmc.v2.domain.score.presentation.data.request.PatchScoreRequest;
import team.incube.gsmc.v2.domain.score.presentation.data.response.GetScoreResponse;
import team.incube.gsmc.v2.domain.score.presentation.data.response.GetScoreSimulateResponse;

/**
 * 점수 관련 HTTP 요청을 처리하는 Web 어댑터 클래스입니다.
 * <p>{@link ScoreApplicationPort}를 통해 점수 조회, 수정, 시뮬레이션 기능을 제공합니다.
 * <p>제공하는 API:
 * <ul>
 *   <li>{@code GET /api/v2/score/current} - 현재 사용자의 점수 조회</li>
 *   <li>{@code GET /api/v2/score/{studentCode}} - 특정 학생의 점수 조회</li>
 *   <li>{@code PATCH /api/v2/score/current} - 현재 사용자의 점수 수정</li>
 *   <li>{@code PATCH /api/v2/score/{studentCode}} - 특정 학생의 점수 수정</li>
 *   <li>{@code POST /api/v2/score/simulate} - 모의 점수 계산</li>
 * </ul>
 * @author snowykte0426
 */
@RestController
@RequestMapping("/api/v2/score")
@RequiredArgsConstructor
public class ScoreWebAdapter {

    private final ScoreApplicationPort scoreApplicationPort;

    @GetMapping("/current")
    public ResponseEntity<GetScoreResponse> getCurrentScore() {
        return ResponseEntity.status(HttpStatus.OK).body(scoreApplicationPort.findCurrentScore());
    }

    @GetMapping("/{studentCode}")
    public ResponseEntity<GetScoreResponse> getScoreByStudentCode(@PathVariable(value = "studentCode") String studentCode) {
        return ResponseEntity.status(HttpStatus.OK).body(scoreApplicationPort.findScoreByEmail(studentCode));
    }

    @PatchMapping("/current")
    public ResponseEntity<Void> updateCurrentScore(@Valid @RequestBody PatchScoreRequest request) {
        scoreApplicationPort.updateCurrentScore(request.categoryName(), request.value());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{studentCode}")
    public ResponseEntity<Void> updateScoreByStudentCode(@PathVariable(value = "studentCode") String studentCode, @Valid @RequestBody PatchScoreRequest request) {
        scoreApplicationPort.updateScoreByEmail(studentCode, request.categoryName(), request.value());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/simulate")
    ResponseEntity<GetScoreSimulateResponse> simulateScore(@Valid @RequestBody GetScoreSimulateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(scoreApplicationPort.simulateScore(
                request.majorAwardCareerOutSchoolOfficial(),
                request.majorAwardCareerOutSchoolUnofficial(),
                request.majorAwardCareerOutSchoolHackathon(),
                request.majorAwardCareerInSchoolGsmfest(),
                request.majorAwardCareerInSchoolSchoolHackathon(),
                request.majorAwardCareerInSchoolPresentation(),
                request.majorCertificateNum(),
                request.majorTopcitScore(),
                request.majorClubAttendanceSemester1(),
                request.majorClubAttendanceSemester2(),
                request.majorOutSchoolAttendanceOfficialContest(),
                request.majorOutSchoolAttendanceUnofficialContest(),
                request.majorOutSchoolAttendanceHackathon(),
                request.majorOutSchoolAttendanceSeminar(),
                request.majorInSchoolAttendanceGsmfest(),
                request.majorInSchoolAttendanceHackathon(),
                request.majorInSchoolAttendanceClubPresentation(),
                request.majorInSchoolAttendanceSeminar(),
                request.majorInSchoolAttendanceAfterSchool(),
                request.humanitiesAwardCareerHumanityInSchool(),
                request.humanitiesAwardCareerHumanityOutSchool(),
                request.humanitiesReadingReadAThonTurtle(),
                request.humanitiesReadingReadAThonCrocodile(),
                request.humanitiesReadingReadAThonRabbitOver(),
                request.humanitiesReading(),
                request.humanitiesServiceActivity(),
                request.humanitiesServiceClubSemester1(),
                request.humanitiesServiceClubSemester2(),
                request.humanitiesCertificateChineseCharacter(),
                request.humanitiesCertificateKoreanHistory(),
                request.humanitiesActivitiesNewrrow_S(),
                request.humanitiesActivitiesSelfDirectedActivities(),
                request.foreignLangAttendanceToeicAcademyStatus(),
                request.foreignLangToeicScore(),
                request.foreignLangToeflScore(),
                request.foreignLangTepsScore(),
                request.foreignLangToeicSpeakingLevel(),
                request.foreignLangOpicGrade(),
                request.foreignLangJptScore(),
                request.foreignLangCptScore(),
                request.foreignLangHskGrade()
        ));
    }
}