package team.incude.gsmc.v2.global.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.score.application.port.ScoreApplicationPort;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;

/**
 * {@link ScoreUpdatedEvent} 발생 시 총합 점수 재계산을 비동기적으로 처리하는 이벤트 리스너입니다.
 * <p>이 리스너는 점수가 변경된 학생의 {@code studentCode}를 기준으로 {@link ScoreApplicationPort#calculateTotalScore(String)}를 호출하여
 * 총합 점수를 즉시 재계산합니다.
 * <p>{@code @Async} 어노테이션이 적용되어 있어 비동기적으로 실행되며,
 * 애플리케이션의 주 흐름에 영향을 주지 않고 백그라운드에서 처리됩니다.
 * @author snowykte0426
 */
@Component
@RequiredArgsConstructor
public class ScoreUpdatedEventListener {

    private final ScoreApplicationPort scoreApplicationPort;

    @Async
    @EventListener(ScoreUpdatedEvent.class)
    public void handleScoreUpdatedEvent(ScoreUpdatedEvent event) {
        scoreApplicationPort.calculateTotalScore(event.email());
    }
}