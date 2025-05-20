package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.DraftReadingEvidence;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;
import java.util.UUID;

/**
 * 독서 증빙자료 임시저장에 대한 영속성 처리를 담당하는 포트 인터페이스입니다.
 * <p>도메인 계층이 저장소 구현에 직접 의존하지 않도록 분리하며,
 * 독서 증빙자료의 임시저장 생성, 조회, 삭제 기능을 제공합니다.
 * 주요 기능:
 * <ul>
 *   <li>{@code saveDraftReadingEvidence} - 임시저장 데이터 저장</li>
 *   <li>{@code findDraftReadingEvidenceById} - ID로 단건 조회</li>
 *   <li>{@code deleteDraftReadingEvidenceById} - ID로 삭제</li>
 *   <li>{@code findAllDraftReadingEvidenceByEmail} - 사용자 이메일 기반 전체 조회</li>
 * </ul>
 * 이 포트의 구현체는 어댑터 계층에서 구성되며, Redis 또는 RDB를 기반으로 할 수 있습니다.
 * @author suuuuuuminnnnnn
 */
@Port(direction = PortDirection.OUTBOUND)
public interface DraftReadingEvidencePersistencePort {
    DraftReadingEvidence saveDraftReadingEvidence(DraftReadingEvidence draftReadingEvidence);

    DraftReadingEvidence findDraftReadingEvidenceById(UUID draftId);

    void deleteDraftReadingEvidenceById(UUID draftId);

    List<DraftReadingEvidence> findAllDraftReadingEvidenceByEmail(String email);
}
