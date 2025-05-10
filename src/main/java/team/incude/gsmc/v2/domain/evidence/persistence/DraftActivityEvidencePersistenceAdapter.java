package team.incude.gsmc.v2.domain.evidence.persistence;

import lombok.RequiredArgsConstructor;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.domain.DraftActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.exception.DraftActivityEvidenceNotFoundException;
import team.incude.gsmc.v2.domain.evidence.persistence.mapper.ActivityEvidenceMapper;
import team.incude.gsmc.v2.domain.evidence.persistence.repository.DraftActivityEvidenceRedisRepository;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;

import java.util.List;
import java.util.UUID;

@Adapter(direction = PortDirection.OUTBOUND)
@RequiredArgsConstructor
public class DraftActivityEvidencePersistenceAdapter implements DraftActivityEvidencePersistencePort {

    private final DraftActivityEvidenceRedisRepository draftActivityEvidenceRedisRepository;
    private final ActivityEvidenceMapper activityEvidenceMapper;


    @Override
    public DraftActivityEvidence saveDraftActivityEvidence(DraftActivityEvidence draftActivityEvidence) {
        return activityEvidenceMapper.toDraftDomain(draftActivityEvidenceRedisRepository.save(activityEvidenceMapper.toDraftEntity(draftActivityEvidence)));
    }

    @Override
    public DraftActivityEvidence findDraftActivityEvidenceById(UUID draftId) {
        return draftActivityEvidenceRedisRepository.findById(draftId)
                .map(activityEvidenceMapper::toDraftDomain)
                .orElseThrow(DraftActivityEvidenceNotFoundException::new);
    }

    @Override
    public void deleteDraftActivityEvidenceById(UUID draftId) {
        draftActivityEvidenceRedisRepository.deleteById(draftId);
    }

    @Override
    public List<DraftActivityEvidence> findAllDraftActivityEvidenceByEmail(String email) {
        return draftActivityEvidenceRedisRepository.findByEmail(email)
                .stream()
                .map(activityEvidenceMapper::toDraftDomain)
                .toList();
    }

}
