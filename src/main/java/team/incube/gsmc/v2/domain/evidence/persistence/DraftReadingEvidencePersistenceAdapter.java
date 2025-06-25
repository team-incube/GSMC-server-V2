package team.incube.gsmc.v2.domain.evidence.persistence;

import lombok.RequiredArgsConstructor;
import team.incube.gsmc.v2.domain.evidence.application.port.DraftReadingEvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.domain.DraftReadingEvidence;
import team.incube.gsmc.v2.domain.evidence.exception.DraftReadingEvidenceNotFoundException;
import team.incube.gsmc.v2.domain.evidence.persistence.mapper.ReadingEvidenceMapper;
import team.incube.gsmc.v2.domain.evidence.persistence.repository.DraftReadingEvidenceRedisRepository;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.UUID;

/**
 * 독서 증빙자료 임시저장에 대한 영속성 처리를 담당하는 어댑터 클래스입니다.
 * <p>{@link DraftReadingEvidencePersistencePort}를 구현하며,
 * Redis를 통한 임시저장 데이터의 저장, 조회, 삭제, 전체 조회 기능을 제공합니다.
 * <p>도메인 ↔ 엔티티 매핑은 {@link ReadingEvidenceMapper}를 통해 수행됩니다.
 * @author suuuuuuminnnnnn
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class DraftReadingEvidencePersistenceAdapter implements DraftReadingEvidencePersistencePort {

    private final DraftReadingEvidenceRedisRepository draftReadingEvidenceRedisRepository;
    private final ReadingEvidenceMapper readingEvidenceMapper;

    /**
     * 독서 증빙자료 임시저장을 저장합니다.
     * @param draftReadingEvidence 저장할 도메인 객체
     * @return 저장된 도메인 객체
     */
    @Override
    public DraftReadingEvidence saveDraftReadingEvidence(DraftReadingEvidence draftReadingEvidence) {
        return readingEvidenceMapper.toDraftDomain(draftReadingEvidenceRedisRepository.save(readingEvidenceMapper.toDraftEntity(draftReadingEvidence)));
    }

    /**
     * UUID 기준으로 독서 증빙자료 임시저장을 조회합니다.
     * @param draftId 조회할 임시저장 ID
     * @return 조회된 도메인 객체
     * @throws DraftReadingEvidenceNotFoundException 존재하지 않는 경우 예외 발생
     */
    @Override
    public DraftReadingEvidence findDraftReadingEvidenceById(UUID draftId) {
        return draftReadingEvidenceRedisRepository.findById(draftId)
                .map(readingEvidenceMapper::toDraftDomain)
                .orElseThrow(DraftReadingEvidenceNotFoundException::new);
    }

    /**
     * UUID 기준으로 독서 증빙자료 임시저장을 삭제합니다.
     * @param draftId 삭제할 임시저장 ID
     */
    @Override
    public void deleteDraftReadingEvidenceById(UUID draftId) {
        draftReadingEvidenceRedisRepository.deleteById(draftId);
    }

    /**
     * 사용자 이메일을 기준으로 독서 증빙자료 임시저장 전체 목록을 조회합니다.
     * @param email 사용자 이메일
     * @return 조회된 임시저장 도메인 객체 리스트
     */
    @Override
    public List<DraftReadingEvidence> findAllDraftReadingEvidenceByEmail(String email) {
        return draftReadingEvidenceRedisRepository.findByEmail(email)
                .stream()
                .map(readingEvidenceMapper::toDraftDomain)
                .toList();
    }
}