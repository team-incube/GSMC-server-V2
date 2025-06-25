package team.incube.gsmc.v2.domain.evidence.application.port;

import team.incube.gsmc.v2.domain.evidence.domain.Evidence;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.port.Port;

/**
 * 증빙자료 엔티티의 영속성 처리를 담당하는 포트 인터페이스입니다.
 * <p>증빙자료의 저장, 조회, 삭제, 락을 통한 조회 등의 기능을 정의하며,
 * 도메인 계층이 저장소 구현체에 직접 의존하지 않도록 추상화된 계약을 제공합니다.
 * 주요 기능:
 * <ul>
 *   <li>{@code findEvidenceById} - ID로 증빙자료 단건 조회</li>
 *   <li>{@code saveEvidence} - 증빙자료 저장</li>
 *   <li>{@code deleteEvidenceById} - 증빙자료 삭제</li>
 *   <li>{@code findEvidenceByIdWithLock} - 증빙자료에 비관적 락을 걸고 조회</li>
 * </ul>
 * 해당 포트는 {@link Evidence} 도메인 객체를 기반으로 동작합니다.
 * @author snowykte0426, suuuuuuminnnnnn
 */
@Port(direction = PortDirection.OUTBOUND)
public interface EvidencePersistencePort {
    Evidence findEvidenceById(Long id);

    Evidence saveEvidence(Evidence evidence);

    void deleteEvidenceById(Long evidenceId);

    Evidence findEvidenceByIdWithLock(Long evidenceId);
}