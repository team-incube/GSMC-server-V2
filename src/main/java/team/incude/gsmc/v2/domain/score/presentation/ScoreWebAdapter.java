package team.incude.gsmc.v2.domain.score.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.incude.gsmc.v2.domain.score.application.port.ScoreApplicationPort;
import team.incude.gsmc.v2.domain.score.presentation.data.request.GetScoreSimulateRequest;
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
    public ResponseEntity<Void> updateCurrentScore(@Valid @RequestBody String score) {
        return null;
    }

    @PatchMapping("/{email}")
    public ResponseEntity<Void> updateScoreByEmail(@PathVariable(value = "email") String email,@Valid @RequestBody String score) {
        return null;
    }

    @PostMapping("/simulate")
    ResponseEntity<GetScoreSimulateResponse> simulateScore(@Valid @RequestBody GetScoreSimulateRequest request) {
        return null;
    }
}