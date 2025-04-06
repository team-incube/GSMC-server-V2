package team.incude.gsmc.v2.domain.evidence.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.domain.evidence.domain.ReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.persistence.entity.ReadingEvidenceJpaEntity;
import team.incude.gsmc.v2.global.mapper.GenericMapper;

@Component
@RequiredArgsConstructor
public class ReadingEvidenceMapper implements GenericMapper<ReadingEvidenceJpaEntity, ReadingEvidence> {

    private final EvidenceMapper evidenceMapper;

    @Override
    public ReadingEvidenceJpaEntity toEntity(ReadingEvidence readingEvidence) {
        return ReadingEvidenceJpaEntity.builder()
                .id(evidenceMapper.toEntity(readingEvidence.getId()))
                .title(readingEvidence.getTitle())
                .author(readingEvidence.getAuthor())
                .page(readingEvidence.getPage())
                .content(readingEvidence.getContent())
                .build();
    }

    @Override
    public ReadingEvidence toDomain(ReadingEvidenceJpaEntity readingEvidenceJpaEntity) {
        return ReadingEvidence.builder()
                .id(evidenceMapper.toDomain(readingEvidenceJpaEntity.getId()))
                .title(readingEvidenceJpaEntity.getTitle())
                .author(readingEvidenceJpaEntity.getAuthor())
                .page(readingEvidenceJpaEntity.getPage())
                .content(readingEvidenceJpaEntity.getContent())
                .build();
    }
}