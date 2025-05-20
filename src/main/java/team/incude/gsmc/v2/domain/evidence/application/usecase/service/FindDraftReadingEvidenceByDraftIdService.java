package team.incude.gsmc.v2.domain.evidence.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.evidence.application.port.DraftReadingEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.usecase.FindDraftReadingEvidenceByDraftIdUseCase;
import team.incude.gsmc.v2.domain.evidence.domain.DraftReadingEvidence;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftReadingEvidenceResponse;

import java.util.UUID;

/**
 * 독서 증빙자료 임시저장을 ID로 조회하는 유스케이스 구현 클래스입니다.
 * <p>{@link FindDraftReadingEvidenceByDraftIdUseCase}를 구현하며,
 * 주어진 UUID를 통해 임시저장된 독서 증빙자료를 조회하고 응답 DTO로 변환합니다.
 * @author suuuuuuminnnnnn
 */
@Service
@RequiredArgsConstructor
public class FindDraftReadingEvidenceByDraftIdService implements FindDraftReadingEvidenceByDraftIdUseCase {

    private final DraftReadingEvidencePersistencePort draftReadingEvidencePersistencePort;

    /**
     * UUID를 기반으로 독서 증빙자료 임시저장을 조회합니다.
     * @param draftId 임시저장 UUID
     * @return 조회된 임시저장 독서 증빙자료 응답 DTO
     */
    @Override
    public GetDraftReadingEvidenceResponse execute(UUID draftId) {
        DraftReadingEvidence evidence = draftReadingEvidencePersistencePort.findDraftReadingEvidenceById(draftId);

        return new GetDraftReadingEvidenceResponse(
                evidence.getId(),
                evidence.getTitle(),
                evidence.getAuthor(),
                evidence.getPage(),
                evidence.getContent()
        );
    }
}