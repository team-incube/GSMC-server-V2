package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetDraftEvidenceResponse;

/**
 * 현재 사용자의 임시저장 증빙자료 전체 목록을 조회하는 유스케이스 인터페이스입니다.
 * <p>활동 증빙자료와 독서 증빙자료를 구분하여 반환하며,
 * 클라이언트는 해당 응답을 통해 사용자의 임시저장 관리 화면을 구성할 수 있습니다.
 * 반환값은 {@link GetDraftEvidenceResponse}이며,
 * 활동 및 독서 임시저장 목록이 포함됩니다.
 * @author suuuuuuminnnnnn
 */
public interface FindDraftEvidenceByCurrentUserUseCase {
    GetDraftEvidenceResponse execute();
}
