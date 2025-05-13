package team.incude.gsmc.v2.domain.evidence.persistence;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.DraftReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.exception.DraftReadingEvidenceNotFoundException;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.ReadingEvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.DraftReadingEvidenceRedisRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.UUID;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class DraftReadingEvidencePersistenceAdapter implements DraftReadingEvidencePersistencePort {

    private final DraftReadingEvidenceRedisRepository draftReadingEvidenceRedisRepository;
    private final ReadingEvidenceMapper readingEvidenceMapper;

    @Override
    public DraftReadingEvidence saveDraftReadingEvidence(DraftReadingEvidence draftReadingEvidence) {
        return readingEvidenceMapper.toDraftDomain(draftReadingEvidenceRedisRepository.save(readingEvidenceMapper.toDraftEntity(draftReadingEvidence)));
    }

    @Override
    public DraftReadingEvidence findDraftReadingEvidenceById(UUID draftId) {
        return draftReadingEvidenceRedisRepository.findById(draftId)
                .map(readingEvidenceMapper::toDraftDomain)
                .orElseThrow(DraftReadingEvidenceNotFoundException::new);
    }

    @Override
    public void deleteDraftReadingEvidenceById(UUID draftId) {
        draftReadingEvidenceRedisRepository.deleteById(draftId);
    }

    @Override
    public List<DraftReadingEvidence> findAllDraftReadingEvidenceByEmail(String email) {
        return draftReadingEvidenceRedisRepository.findByEmail(email)
                .stream()
                .map(readingEvidenceMapper::toDraftDomain)
                .toList();
    }
}
