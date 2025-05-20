package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.evidence.presentation.data.response.GetEvidencesResponse;


/**
 * 학생 코드, 증빙자료 타입, 검토 상태를 기준으로 증빙자료를 조회하는 유스케이스 인터페이스입니다.
 * <p>관리자가 특정 학생의 증빙자료 중 타입과 상태 기준으로 필터링하여 확인할 때 사용됩니다.
 * 반환값은 {@link GetEvidencesResponse} 형태이며, 조건에 일치하는 증빙자료 목록을 포함합니다.
 * @author suuuuuuminnnnnn
 */
public interface FindEvidenceByStudentCodeAndFilteringTypeAndStatusUseCase {
    GetEvidencesResponse execute(String studentCode, EvidenceType type, ReviewStatus status);
}
