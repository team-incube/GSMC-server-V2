package team.incude.gsmc.v2.domain.evidence.application.usecase;

import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;

/**
 * 증빙자료의 검토 상태를 변경하는 유스케이스 인터페이스입니다.
 * <p>관리자 또는 검토 권한이 있는 사용자가 해당 증빙자료의 검토 상태를
 * {@link team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus} 값으로 갱신할 수 있도록 정의합니다.
 * @author suuuuuuminnnnnn
 */
public interface UpdateReviewStatusUseCase {
    void execute(Long evidenceId, ReviewStatus reviewStatus);
}
