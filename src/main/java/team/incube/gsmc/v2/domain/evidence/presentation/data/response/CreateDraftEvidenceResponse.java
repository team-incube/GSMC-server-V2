package team.incube.gsmc.v2.domain.evidence.presentation.data.response;

import java.util.UUID;

/**
 * 임시저장 증빙자료 생성 결과를 반환하는 응답 DTO입니다.
 * <p>임시저장된 증빙자료의 식별자인 {@code draftId}를 클라이언트에 반환하여,
 * 이후 수정 또는 정식 등록 시 참조할 수 있도록 합니다.
 * @param draftId 생성된 임시저장 증빙자료의 식별자
 * @author suuuuuuminnnnnn
 */
public record CreateDraftEvidenceResponse(
        UUID draftId
) {
}
