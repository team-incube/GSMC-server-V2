package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftActivityEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.FindDraftActivityEvidenceByDraftIdUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.DraftActivityEvidence;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftActivityEvidenceResponse;

import java.util.UUID;

/**
 * 활동 증빙자료 임시저장을 ID로 조회하는 유스케이스 구현 클래스입니다.
 * <p>{@link FindDraftActivityEvidenceByDraftIdUseCase}를 구현하며,
 * 주어진 UUID를 통해 Redis 또는 저장소에서 임시저장된 활동 증빙자료를 조회합니다.
 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
public class FindDraftActivityEvidenceByDraftIdService implements FindDraftActivityEvidenceByDraftIdUseCase {

    private final DraftActivityEvidencePersistencePort draftActivityEvidencePersistencePort;

    /**
     * UUID로 활동 증빙자료 임시저장을 조회합니다.
     * @param draftId 조회할 임시저장 ID
     * @return 조회된 활동 증빙자료 응답 DTO
     */
    @Override
    public GetDraftActivityEvidenceResponse execute(UUID draftId) {
        DraftActivityEvidence evidence = draftActivityEvidencePersistencePort.findDraftActivityEvidenceById(draftId);

        return new GetDraftActivityEvidenceResponse(
                evidence.getId(),
                evidence.getCategoryName(),
                evidence.getTitle(),
                evidence.getContent(),
                evidence.getImageUrl(),
                evidence.getEvidenceType());
    }
}
