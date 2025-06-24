package team.incube.gsmc.v2.domain.evidence.application.usecase;

import org.springframework.web.multipart.MultipartFile;

/**
 * 점수 기반 기타 증빙자료 생성을 위한 유스케이스 인터페이스입니다.
 * <p>카테고리 이름, 첨부 파일, 점수 값을 기반으로 점수화 가능한 기타 증빙자료를 생성합니다.
 * 해당 증빙자료는 사용자 활동을 수치화하여 평가 항목에 반영하기 위해 사용됩니다.
 * 이 유스케이스는 {@code EvidenceApplicationAdapter}를 통해 호출되며,
 * 도메인 서비스에서 구현됩니다.
 * @author suuuuuuminnnnnn
 */
public interface CreateOtherScoringEvidenceUseCase {
    void execute(String categoryName, MultipartFile file, int value);
}
