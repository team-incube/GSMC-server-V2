package team.incude.gsmc.v2.domain.evidence.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.OtherEvidenceJpaEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

/**
 * 기타 증빙자료에 대한 도메인 ↔ JPA 엔티티 매핑을 담당하는 매퍼 클래스입니다.
 * <p>{@link OtherEvidenceJpaEntity}와 {@link OtherEvidence} 간의 상호 변환을 지원합니다.
 * <p>내부적으로 {@link EvidenceMapper}를 활용하여 공통 Evidence 정보를 함께 매핑합니다.
 * {@link team.incude.gsmc.v2.global.mapper.GenericMapper}를 구현하여 공통 매핑 인터페이스를 따릅니다.
 * @author suuuuuuminnnnnn
 */
@Component
@RequiredArgsConstructor
public class OtherEvidenceMapper implements GenericMapper<OtherEvidenceJpaEntity, OtherEvidence> {

    private final EvidenceMapper evidenceMapper;

    /**
     * 도메인 객체 {@link OtherEvidence}를 JPA 엔티티 {@link OtherEvidenceJpaEntity}로 변환합니다.
     * @param otherEvidence 변환할 도메인 객체
     * @return 변환된 JPA 엔티티
     */
    @Override
    public OtherEvidenceJpaEntity toEntity(OtherEvidence otherEvidence) {
        return OtherEvidenceJpaEntity.builder()
                .id(otherEvidence.getId().getId())
                .evidence(evidenceMapper.toEntity(otherEvidence.getId()))
                .fileUri(otherEvidence.getFileUri())
                .build();
    }

    /**
     * JPA 엔티티 {@link OtherEvidenceJpaEntity}를 도메인 객체 {@link OtherEvidence}로 변환합니다.
     * @param otherEvidenceJpaEntity 변환할 JPA 엔티티
     * @return 변환된 도메인 객체
     */
    @Override
    public OtherEvidence toDomain(OtherEvidenceJpaEntity otherEvidenceJpaEntity) {
        return OtherEvidence.builder()
                .id(evidenceMapper.toDomain(otherEvidenceJpaEntity.getEvidence()))
                .fileUri(otherEvidenceJpaEntity.getFileUri())
                .build();
    }
}