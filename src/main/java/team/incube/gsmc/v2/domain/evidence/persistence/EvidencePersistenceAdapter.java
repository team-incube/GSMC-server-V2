package team.incube.gsmc.v2.domain.evidence.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import team.incube.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incube.gsmc.v2.domain.evidence.domain.Evidence;
import team.incube.gsmc.v2.domain.evidence.exception.EvidenceNotFoundException;
import team.incube.gsmc.v2.domain.evidence.persistence.mapper.EvidenceMapper;
import team.incube.gsmc.v2.domain.evidence.persistence.repository.EvidenceJpaRepository;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.Optional;

import static team.incube.gsmc.v2.domain.evidence.persistence.entity.QEvidenceJpaEntity.evidenceJpaEntity;

/**
 * 증빙자료의 영속성 처리를 담당하는 어댑터 클래스입니다.
 * <p>{@link EvidencePersistencePort}를 구현하며, 증빙자료의 저장, 조회, 삭제, 비관적 락 조회 기능을 제공합니다.
 * <p>JPA 및 QueryDSL을 기반으로 도메인 ↔ 엔티티 변환은 {@link EvidenceMapper}를 통해 수행됩니다.
 * 주요 기능:
 * <ul>
 *     <li>단건 조회 (with/without Lock)</li>
 *     <li>증빙자료 저장</li>
 *     <li>증빙자료 삭제</li>
 * </ul>
 * @author snowykte0426, suuuuuuminnnnnn
 */
@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class EvidencePersistenceAdapter implements EvidencePersistencePort {

    private final EvidenceJpaRepository evidenceJpaRepository;
    private final EvidenceMapper evidenceMapper;
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 증빙자료를 저장합니다.
     * @param evidence 저장할 도메인 증빙자료 객체
     * @return 저장된 도메인 객체
     */
    @Override
    public Evidence saveEvidence(Evidence evidence) {
        return evidenceMapper.toDomain(evidenceJpaRepository.save(evidenceMapper.toEntity(evidence)));
    }

    /**
     * 증빙자료 ID로 단건 조회합니다.
     *
     * @param id 조회할 증빙자료 ID
     * @return 조회된 도메인 객체
     * @throws EvidenceNotFoundException 존재하지 않을 경우
     */
    @Override
    public Evidence findEvidenceById(Long id) {
        return evidenceJpaRepository.findById(id)
                .map(evidenceMapper::toDomain)
                .orElseThrow(EvidenceNotFoundException::new);
    }

    /**
     * 증빙자료 ID로 해당 증빙자료를 삭제합니다.
     *
     * @param id 삭제할 증빙자료 ID
     */
    @Override
    public void deleteEvidenceById(Long id) {
        evidenceJpaRepository.deleteById(id);
    }

    /**
     * 증빙자료 ID로 비관적 락을 걸어 단건 조회합니다.
     *
     * @param id 조회할 증빙자료 ID
     * @return 조회된 도메인 객체
     * @throws EvidenceNotFoundException 존재하지 않을 경우
     */
    @Override
    public Evidence findEvidenceByIdWithLock(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(evidenceJpaEntity)
                        .where(evidenceJpaEntity.id.eq(id))
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .fetchOne()
                ).map(evidenceMapper::toDomain).orElseThrow(EvidenceNotFoundException::new);

    }
}