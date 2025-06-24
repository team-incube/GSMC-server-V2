package team.incube.gsmc.v2.domain.evidence.application.port;

import team.incube.gsmc.v2.domain.evidence.domain.ActivityEvidence;
import team.incube.gsmc.v2.domain.evidence.domain.Evidence;
import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incube.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.port.Port;

import java.util.List;

/**
 * 활동 증빙자료에 대한 영속성 처리를 정의하는 포트 인터페이스입니다.
 * <p>도메인 계층이 인프라에 직접 의존하지 않도록 추상화된 접근 방식을 제공합니다.
 * <p>이 포트를 구현하는 어댑터는 활동 증빙자료의 조회, 검색, 저장, 삭제 기능을 담당합니다.
 * 주요 기능:
 * <ul>
 *   <li>사용자 이메일과 증빙자료 유형에 따른 조회</li>
 *   <li>학번, 제목, 검토 상태, 학년/반 기준의 검색</li>
 *   <li>이메일, 제목, 증빙자료 타입 기준의 검색</li>
 *   <li>이메일 기준의 검색</li>
 *   <li>활동 증빙자료 저장 및 삭제</li>
 *   <li>단건 조회</li>
 * </ul>
 * @author suuuuuuminnnnnn
 */
@Port(direction = PortDirection.OUTBOUND)
public interface ActivityEvidencePersistencePort {
    List<ActivityEvidence> findActivityEvidenceByEmailAndEvidenceType(String email, EvidenceType evidenceType);

    List<ActivityEvidence> findActivityEvidenceByMemberEmailAndTitleAndType(String title, String email, EvidenceType evidenceType);

    List<ActivityEvidence> findActivityEvidenceByMemberEmailAndTypeAndStatus(String email, EvidenceType type, ReviewStatus status);

    List<ActivityEvidence> findActivityEvidenceByMemberEmail(String memberEmail);

    ActivityEvidence saveActivityEvidence(ActivityEvidence activityEvidence);

    ActivityEvidence saveActivityEvidence(Evidence evidence, ActivityEvidence activityEvidence);

    void deleteActivityEvidenceById(Long evidenceId);

    ActivityEvidence findActivityEvidenceById(Long id);

    ActivityEvidence findActivityEvidenceByIdOrNull(Long id);
}