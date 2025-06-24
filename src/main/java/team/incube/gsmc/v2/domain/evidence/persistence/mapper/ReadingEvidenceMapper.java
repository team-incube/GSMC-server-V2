package team.incube.gsmc.v2.domain.evidence.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incube.gsmc.v2.domain.evidence.domain.DraftReadingEvidence;
import team.incube.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incube.gsmc.v2.domain.evidence.persistence.entity.DraftReadingEvidenceRedisEntity;
import team.incube.gsmc.v2.domain.evidence.persistence.entity.ReadingEvidenceJpaEntity;
import team.incube.gsmc.v2.global.mapper.GenericMapper;

/**
 * 독서 증빙자료에 대한 도메인 ↔ JPA/Redis 엔티티 매핑을 담당하는 매퍼 클래스입니다.
 * <p>JPA 기반 {@link ReadingEvidenceJpaEntity} 및 Redis 기반 {@link DraftReadingEvidenceRedisEntity}를
 * 도메인 객체 {@link ReadingEvidence} 및 {@link DraftReadingEvidence}와 상호 변환합니다.
 * <p>{@link GenericMapper}를 구현하여 공통 매핑 로직을 제공합니다.
 * @author suuuuuuminnnnnn
 */
@Component
@RequiredArgsConstructor
public class ReadingEvidenceMapper implements GenericMapper<ReadingEvidenceJpaEntity, ReadingEvidence> {

    private final EvidenceMapper evidenceMapper;

    /**
     * 도메인 객체 {@link ReadingEvidence}를 JPA 엔티티로 변환합니다.
     * @param readingEvidence 도메인 객체
     * @return 변환된 JPA 엔티티
     */
    @Override
    public ReadingEvidenceJpaEntity toEntity(ReadingEvidence readingEvidence) {
        return ReadingEvidenceJpaEntity.builder()
                .id(readingEvidence.getId().getId())
                .evidence(evidenceMapper.toEntity(readingEvidence.getId()))
                .title(readingEvidence.getTitle())
                .author(readingEvidence.getAuthor())
                .page(readingEvidence.getPage())
                .content(readingEvidence.getContent())
                .build();
    }

    /**
     * JPA 엔티티 {@link ReadingEvidenceJpaEntity}를 도메인 객체로 변환합니다.
     * @param readingEvidenceJpaEntity JPA 엔티티
     * @return 변환된 도메인 객체
     */
    @Override
    public ReadingEvidence toDomain(ReadingEvidenceJpaEntity readingEvidenceJpaEntity) {
        return ReadingEvidence.builder()
                .id(evidenceMapper.toDomain(readingEvidenceJpaEntity.getEvidence()))
                .title(readingEvidenceJpaEntity.getTitle())
                .author(readingEvidenceJpaEntity.getAuthor())
                .page(readingEvidenceJpaEntity.getPage())
                .content(readingEvidenceJpaEntity.getContent())
                .build();
    }

    /**
     * 도메인 객체 {@link DraftReadingEvidence}를 Redis 임시저장 엔티티로 변환합니다.
     * @param draftReadingEvidence 도메인 객체
     * @return 변환된 Redis 엔티티
     */
    public DraftReadingEvidenceRedisEntity toDraftEntity(DraftReadingEvidence draftReadingEvidence) {
        return DraftReadingEvidenceRedisEntity.builder()
                .id(draftReadingEvidence.getId())
                .title(draftReadingEvidence.getTitle())
                .author(draftReadingEvidence.getAuthor())
                .page(draftReadingEvidence.getPage())
                .content(draftReadingEvidence.getContent())
                .ttl(draftReadingEvidence.getTtl())
                .email(draftReadingEvidence.getEmail())
                .build();
    }

    /**
     * Redis 임시저장 엔티티 {@link DraftReadingEvidenceRedisEntity}를 도메인 객체로 변환합니다.
     * @param draftReadingEvidenceRedisEntity Redis 임시저장 엔티티
     * @return 변환된 도메인 객체
     */
    public DraftReadingEvidence toDraftDomain(DraftReadingEvidenceRedisEntity draftReadingEvidenceRedisEntity) {
        return DraftReadingEvidence.builder()
                .id(draftReadingEvidenceRedisEntity.getId())
                .title(draftReadingEvidenceRedisEntity.getTitle())
                .author(draftReadingEvidenceRedisEntity.getAuthor())
                .page(draftReadingEvidenceRedisEntity.getPage())
                .content(draftReadingEvidenceRedisEntity.getContent())
                .ttl(draftReadingEvidenceRedisEntity.getTtl())
                .email(draftReadingEvidenceRedisEntity.getEmail())
                .build();
    }
}