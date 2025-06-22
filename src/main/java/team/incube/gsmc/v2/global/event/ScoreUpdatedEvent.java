package team.incube.gsmc.v2.global.event;

/**
 * 점수 변경이 발생했을 때 발행되는 도메인 이벤트입니다.
 * <p>총점 재계산 등의 후속 작업을 수행하기 위해 사용됩니다.
 * {@code studentCode}를 통해 점수가 변경된 학생을 식별할 수 있습니다.
 * 이 이벤트는 {@link org.springframework.context.ApplicationEventPublisher}를 통해 발행되며,
 * {@code @EventListener}로 후속 로직에서 구독할 수 있습니다.
 *
 * @author snowykte0426
 */
public record ScoreUpdatedEvent(String email) {
}