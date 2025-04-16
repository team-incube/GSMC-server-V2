package team.incude.gsmc.v2.domain.score.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.incude.gsmc.v2.domain.score.application.port.ScoreApplicationPort;
import team.incude.gsmc.v2.domain.score.presentation.data.request.GetScoreSimulateRequest;
import team.incude.gsmc.v2.domain.score.presentation.data.request.PatchScoreRequest;
import team.incude.gsmc.v2.domain.score.presentation.data.response.GetScoreResponse;
import team.incude.gsmc.v2.domain.score.presentation.data.response.GetScoreSimulateResponse;

@RestController
@RequestMapping("/api/v2/score")
@RequiredArgsConstructor
public class ScoreWebAdapter {

    private final ScoreApplicationPort scoreApplicationPort;

    @GetMapping("/current")
    public ResponseEntity<GetScoreResponse> getCurrentScore() {
        return ResponseEntity.status(HttpStatus.OK).body(scoreApplicationPort.findCurrentScore());
    }

    @GetMapping("/{email}")
    public ResponseEntity<GetScoreResponse> getScoreByEmail(@PathVariable(value = "email") String email) {
        return ResponseEntity.status(HttpStatus.OK).body(scoreApplicationPort.findScoreByEmail(email));
    }

    @PatchMapping("/current")
    public ResponseEntity<Void> updateCurrentScore(@Valid @RequestBody PatchScoreRequest request) {
        scoreApplicationPort.updateCurrentScore(request.categoryName(), request.value());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{email}")
    public ResponseEntity<Void> updateScoreByEmail(@PathVariable(value = "email") String email, @Valid @RequestBody PatchScoreRequest request) {
        scoreApplicationPort.updateScoreByEmail(email, request.categoryName(), request.value());
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
                request.humanitiesActivitiesSelfDirectedActivities(),
                request.humanitiesActivitiesNewrrowS_S(),
                request.foreignLangAttendanceToeicAcademyStatus(),
                request.foreignLangToeicScore(),
                request.foreignLangToeflScore(),
                request.foreignLangTepsScore(),
                request.foreignLangToeicSpeakingLevel(),
                request.foreignLangOpicGrade(),
                request.foreignLangJptScore(),
                request.foreignLangCptScore(),
                request.foreignLangHskScore()
        ));
    }
}