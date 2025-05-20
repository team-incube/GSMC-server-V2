package team.incude.gsmc.v2.domain.evidence.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.EvidenceJpaEntity;
import team.incude.gsmc.v2.domain.score.persistence.mapper.ScoreMapper;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

/**
 * 공통 Evidence 정보에 대한 도메인 ↔ JPA 매핑을 담당하는 매퍼 클래스입니다.
 * <p>{@link EvidenceJpaEntity}와 {@link Evidence} 간의 변환을 수행하며,
 * 내부적으로 {@link ScoreMapper}를 활용하여 Score 정보도 함께 매핑됩니다.
 * <p>이 클래스는 {@link team.incude.gsmc.v2.global.mapper.GenericMapper}를 구현하여
 * 공통 매핑 패턴을 따릅니다.
 * @author suuuuuuminnnnnn
 */
@Component
@RequiredArgsConstructor
public class EvidenceMapper implements GenericMapper<EvidenceJpaEntity, Evidence> {

    private final ScoreMapper scoreMapper;

    /**
     * 도메인 객체 {@link Evidence}를 JPA 엔티티 {@link EvidenceJpaEntity}로 변환합니다.
     * @param evidence 도메인 객체
     * @return 변환된 JPA 엔티티
     */
    @Override
    public EvidenceJpaEntity toEntity(Evidence evidence) {
        return EvidenceJpaEntity.builder()
                .id(evidence.getId())
                .score(scoreMapper.toEntity(evidence.getScore()))
                .evidenceType(evidence.getEvidenceType())
                .reviewStatus(evidence.getReviewStatus())
                .createdAt(evidence.getCreatedAt())
                .updatedAt(evidence.getUpdatedAt())
                .build();
    }

    /**
     * JPA 엔티티 {@link EvidenceJpaEntity}를 도메인 객체 {@link Evidence}로 변환합니다.
     * @param evidenceJpaEntity JPA 엔티티
     * @return 변환된 도메인 객체
     */
    @Override
    public Evidence toDomain(EvidenceJpaEntity evidenceJpaEntity) {
        return Evidence.builder()
                .id(evidenceJpaEntity.getId())
                .score(scoreMapper.toDomain(evidenceJpaEntity.getScore()))
                .evidenceType(evidenceJpaEntity.getEvidenceType())
                .reviewStatus(evidenceJpaEntity.getReviewStatus())
                .createdAt(evidenceJpaEntity.getCreatedAt())
                .updatedAt(evidenceJpaEntity.getUpdatedAt())
                .build();
    }
}