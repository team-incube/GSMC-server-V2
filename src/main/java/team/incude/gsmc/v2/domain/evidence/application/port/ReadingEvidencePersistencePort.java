package team.incude.gsmc.v2.domain.evidence.application.port;

import team.incude.gsmc.v2.domain.evidence.domain.DraftReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;
import java.util.UUID;

/**
 * 독서 증빙자료의 영속성 처리를 담당하는 포트 인터페이스입니다.
 * <p>도메인 계층이 저장소 구현체에 직접 의존하지 않도록 분리하며,
 * 독서 증빙자료의 저장, 조회, 검색, 삭제 기능을 제공합니다.
 * 주요 기능:
 * <ul>
 *   <li>{@code saveReadingEvidence} - 독서 증빙자료 저장</li>
 *   <li>{@code findReadingEvidenceByEmail} - 사용자 이메일 기반 조회</li>
 *   <li>{@code searchReadingEvidence} - 조건 기반 다중 검색 (학번, 제목, 상태 등)</li>
 *   <li>{@code findReadingEvidenceById} - ID 기반 단건 조회</li>
 *   <li>{@code deleteReadingEvidenceById} - 증빙자료 삭제</li>
 * </ul>
 * 이 포트의 구현체는 어댑터 계층에 위치합니다.
 * @author suuuuuuminnnnnn
 */
@Port(direction = PortDirection.OUTBOUND)
public interface ReadingEvidencePersistencePort {
    List<ReadingEvidence> findReadingEvidenceByEmail(String email);

    ReadingEvidence saveReadingEvidence(ReadingEvidence readingEvidence);

    ReadingEvidence saveReadingEvidence(Evidence evidence, ReadingEvidence readingEvidence);

    List<ReadingEvidence> searchReadingEvidence(String studentCode, String title, EvidenceType evidenceType, ReviewStatus status, Integer grade, Integer classNumber);

    void deleteReadingEvidenceById(Long evidenceId);

    ReadingEvidence findReadingEvidenceById(Long id);
}