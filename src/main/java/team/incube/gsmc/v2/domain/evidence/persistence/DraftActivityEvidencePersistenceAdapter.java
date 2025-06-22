package team.incube.gsmc.v2.domain.evidence.persistence;

import lombok.RequiredArgsConstructor;
import team.incube.gsmc.v2.domain.evidence.application.port.DraftActivityEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.domain.DraftActivityEvidence;
import team.incube.gsmc.v2.domain.evidence.exception.DraftActivityEvidenceNotFoundException;
import team.incube.gsmc.v2.domain.evidence.persistence.mapper.ActivityEvidenceMapper;
import team.incube.gsmc.v2.domain.evidence.persistence.repository.DraftActivityEvidenceRedisRepository;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.UUID;

/**
 * 활동 증빙자료 임시저장에 대한 영속성 처리를 담당하는 어댑터 클래스입니다.
 * <p>{@link DraftActivityEvidencePersistencePort}를 구현하며,
 * Redis를 통한 임시저장 데이터의 저장, 조회, 삭제, 전체 조회 기능을 제공합니다.
 * <p>도메인 ↔ 엔티티 매핑은 {@link ActivityEvidenceMapper}를 통해 수행됩니다.
 * @author suuuuuuminnnnnn
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class DraftActivityEvidencePersistenceAdapter implements DraftActivityEvidencePersistencePort {

    private final DraftActivityEvidenceRedisRepository draftActivityEvidenceRedisRepository;
    private final ActivityEvidenceMapper activityEvidenceMapper;

    /**
     * 활동 증빙자료 임시저장을 저장합니다.
     * @param draftActivityEvidence 저장할 도메인 객체
     * @return 저장된 도메인 객체
     */
    @Override
    public DraftActivityEvidence saveDraftActivityEvidence(DraftActivityEvidence draftActivityEvidence) {
        return activityEvidenceMapper.toDraftDomain(draftActivityEvidenceRedisRepository.save(activityEvidenceMapper.toDraftEntity(draftActivityEvidence)));
    }
    /**
     * UUID 기준으로 활동 증빙자료 임시저장을 조회합니다.
     * @param draftId 조회할 임시저장 ID
     * @return 조회된 도메인 객체
     * @throws DraftActivityEvidenceNotFoundException 존재하지 않는 경우 예외 발생
     */
    @Override
    public DraftActivityEvidence findDraftActivityEvidenceById(UUID draftId) {
        return draftActivityEvidenceRedisRepository.findById(draftId)
                .map(activityEvidenceMapper::toDraftDomain)
                .orElseThrow(DraftActivityEvidenceNotFoundException::new);
    }
    /**
     * UUID 기준으로 활동 증빙자료 임시저장을 삭제합니다.
     * @param draftId 삭제할 임시저장 ID
     */
    @Override
    public void deleteDraftActivityEvidenceById(UUID draftId) {
        draftActivityEvidenceRedisRepository.deleteById(draftId);
    }
    /**
     * 사용자 이메일을 기준으로 활동 증빙자료 임시저장 전체 목록을 조회합니다.
     * @param email 사용자 이메일
     * @return 조회된 임시저장 도메인 객체 리스트
     */
    @Override
    public List<DraftActivityEvidence> findAllDraftActivityEvidenceByEmail(String email) {
        return draftActivityEvidenceRedisRepository.findByEmail(email)
                .stream()
                .map(activityEvidenceMapper::toDraftDomain)
                .toList();
    }
}