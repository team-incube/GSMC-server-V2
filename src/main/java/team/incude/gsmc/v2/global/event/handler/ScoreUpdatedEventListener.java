package team.incude.gsmc.v2.global.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.score.application.port.ScoreApplicationPort;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;

@Component
@RequiredArgsConstructor
public class ScoreUpdatedEventListener {

    private final ScoreApplicationPort scoreApplicationPort;

    @Async
    @EventListener
    public void handleScoreUpdatedEvent(ScoreUpdatedEvent event) {
        scoreApplicationPort.calculateTotalScore(event.getStudentCode());
    }
}