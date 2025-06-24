package team.incube.gsmc.v2.domain.evidence.presentation.data.response;

import java.util.List;

/**
 * 사용자의 전체 임시저장 증빙자료 목록을 반환하는 응답 DTO입니다.
 * <p>활동 증빙자료와 독서 증빙자료의 임시저장 데이터를 각각 리스트로 제공합니다.
 * 클라이언트는 이 응답을 기반으로 사용자의 임시저장 목록 화면을 구성할 수 있습니다.
 * @param activityEvidences 활동 증빙자료 임시저장 목록
 * @param readingEvidences 독서 증빙자료 임시저장 목록
 * @author suuuuuuminnnnnn
 */
public record GetDraftEvidenceResponse(
        List<GetDraftActivityEvidenceResponse> activityEvidences,
        List<GetDraftReadingEvidenceResponse> readingEvidences
) {
}
