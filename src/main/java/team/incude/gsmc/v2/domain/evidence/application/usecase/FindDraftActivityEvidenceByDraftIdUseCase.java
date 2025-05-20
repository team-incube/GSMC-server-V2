package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftActivityEvidenceResponse;

import java.util.UUID;

/**
 * 활동 증빙자료 임시저장을 ID로 조회하는 유스케이스 인터페이스입니다.
 * <p>임시저장된 활동 증빙자료의 ID(UUID)를 기준으로 단건 조회하며,
 * 클라이언트가 해당 임시저장을 편집하거나 복원할 수 있도록 정보를 반환합니다.
 * 반환값은 {@link GetDraftActivityEvidenceResponse} 형식이며, 제목, 내용, 이미지, 활동 유형 등을 포함합니다.
 * @author suuuuuuminnnnnn
 */
public interface FindDraftActivityEvidenceByDraftIdUseCase {
    GetDraftActivityEvidenceResponse execute(UUID draftId);
}
