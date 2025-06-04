package team.incude.gsmc.v2.global.event;

import java.util.UUID;

/**
 * 임시 저장 증빙 삭제 이벤트를 나타내는 레코드 클래스입니다.
 * <p>이 이벤트는 특정 임시 저장 ID에 대한 증빙 삭제 작업을 수행하기 위해 사용됩니다.
 * {@code draftId}를 통해 삭제할 임시 저장 증빙을 식별합니다.
 * @param draftId 삭제할 임시 저장 증빙의 ID
 */
public record DraftEvidenceDeleteEvent(UUID draftId) {
}