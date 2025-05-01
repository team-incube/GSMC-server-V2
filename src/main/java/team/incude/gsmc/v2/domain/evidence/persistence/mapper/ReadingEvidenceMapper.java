package team.incude.gsmc.v2.domain.evidence.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.evidence.domain.DraftReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.DraftReadingEvidenceRedisEntity;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.ReadingEvidenceJpaEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

@Component
@RequiredArgsConstructor
public class ReadingEvidenceMapper implements GenericMapper<ReadingEvidenceJpaEntity, ReadingEvidence> {

    private final EvidenceMapper evidenceMapper;

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

    public DraftReadingEvidenceRedisEntity toDraftEntity(DraftReadingEvidence draftReadingEvidence) {
        return DraftReadingEvidenceRedisEntity.builder()
                .id(draftReadingEvidence.getId())
                .title(draftReadingEvidence.getTitle())
                .author(draftReadingEvidence.getAuthor())
                .page(draftReadingEvidence.getPage())
                .content(draftReadingEvidence.getContent())
                .ttl(draftReadingEvidence.getTtl())
                .build();
    }

    public DraftReadingEvidence toDraftDomain(DraftReadingEvidenceRedisEntity draftReadingEvidenceRedisEntity) {
        return DraftReadingEvidence.builder()
                .id(draftReadingEvidenceRedisEntity.getId())
                .title(draftReadingEvidenceRedisEntity.getTitle())
                .author(draftReadingEvidenceRedisEntity.getAuthor())
                .page(draftReadingEvidenceRedisEntity.getPage())
                .content(draftReadingEvidenceRedisEntity.getContent())
                .ttl(draftReadingEvidenceRedisEntity.getTtl())
                .build();
    }
}