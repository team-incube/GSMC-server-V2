package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

/**
 * 기타 증빙자료의 영속성 처리를 담당하는 포트 인터페이스입니다.
 * <p>도메인 계층이 저장소 구현체에 직접 의존하지 않도록 분리하며,
 * 기타 증빙자료의 저장, 조회, 검색, 삭제 기능을 제공합니다.
 * 주요 기능:
 * <ul>
 *   <li>{@code saveOtherEvidence} - 기타 증빙자료 저장</li>
 *   <li>{@code findOtherEvidenceByEmail} - 사용자 이메일 기준 목록 조회</li>
 *   <li>{@code findOtherEvidenceByMemberEmailAndType} - 사용자 이메일 및 증빙자료 타입 기준 목록 조회</li>
 *   <li>{@code searchOtherEvidence} - 학생코드, 타입, 상태, 학년/반 기준 조건 검색</li>
 *   <li>{@code deleteOtherEvidenceById} - ID 기반 삭제</li>
 *   <li>{@code findOtherEvidenceById} - ID 기반 단건 조회</li>
 * </ul>
 * 이 포트는 {@code @Port(direction = OUTBOUND)}로 지정되며,
 * 구현체는 어댑터 계층에 위치합니다.
 * @author snowykte0426, suuuuuuminnnnnn
 */
@Port(direction = PortDirection.OUTBOUND)
public interface OtherEvidencePersistencePort {
    OtherEvidence saveOtherEvidence(OtherEvidence evidence);

    OtherEvidence saveOtherEvidence(Evidence evidence, OtherEvidence otherEvidence);

    List<OtherEvidence> findOtherEvidenceByEmail(String email);

    List<OtherEvidence> findOtherEvidenceByMemberEmailAndType(String email, EvidenceType type);

    List<OtherEvidence> findOtherEvidenceByMemberEmailAndTypeAndStatus(String email, EvidenceType type, ReviewStatus status);

    void deleteOtherEvidenceById(Long evidenceId);

    OtherEvidence findOtherEvidenceById(Long id);

    OtherEvidence findOtherEvidenceByIdOrNull(Long id);
}