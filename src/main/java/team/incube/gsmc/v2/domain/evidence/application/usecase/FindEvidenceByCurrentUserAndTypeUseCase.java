package team.incube.gsmc.v2.domain.evidence.application.usecase;

import team.incube.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incube.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;

/**
 * 현재 사용자의 증빙자료 중 특정 타입에 해당하는 목록을 조회하는 유스케이스 인터페이스입니다.
 * <p>조회할 증빙자료의 타입은 {@link EvidenceType}으로 지정되며,
 * 활동, 독서, 기타 등의 유형 중 하나를 선택할 수 있습니다.
 * <p>응답은 {@link GetEvidencesResponse} 형태로 반환되며,
 * 해당 타입에 해당하는 증빙자료만 필터링되어 포함됩니다.
 * @author suuuuuuminnnnnn
 */
public interface FindEvidenceByCurrentUserAndTypeUseCase {
    GetEvidencesResponse execute(EvidenceType type);
}
