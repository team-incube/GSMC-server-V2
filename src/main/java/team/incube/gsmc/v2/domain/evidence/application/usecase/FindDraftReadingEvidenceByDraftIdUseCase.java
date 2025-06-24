package team.incube.gsmc.v2.domain.evidence.application.usecase;

import team.incube.gsmc.v2.domain.evidence.presentation.data.response.GetDraftReadingEvidenceResponse;

import java.util.UUID;

/**
 * 독서 증빙자료 임시저장을 ID(UUID)로 조회하는 유스케이스 인터페이스입니다.
 * <p>클라이언트는 이 유스케이스를 통해 특정 임시저장 ID에 해당하는 독서 증빙자료의 세부 정보를 조회할 수 있습니다.
 * 반환된 정보는 편집 또는 등록 화면에서 활용됩니다.
 * 반환값은 {@link GetDraftReadingEvidenceResponse}이며,
 * 제목, 저자, 페이지 수, 독서 내용 등의 정보를 포함합니다.
 * @author suuuuuuminnnnnn
 */
public interface FindDraftReadingEvidenceByDraftIdUseCase {
    GetDraftReadingEvidenceResponse execute(UUID draftId);
}
