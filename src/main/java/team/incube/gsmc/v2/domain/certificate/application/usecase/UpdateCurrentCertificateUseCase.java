package team.incube.gsmc.v2.domain.certificate.application.usecase;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * 현재 로그인한 사용자의 자격증 수정 유스케이스를 정의하는 인터페이스입니다.
 * <p>자격증 이름, 취득일, 증빙 파일을 수정할 수 있으며,
 * 이 인터페이스는 서비스 계층에서 실제 구현되어 사용됩니다.
 * <p>비즈니스 규칙:
 * <ul>
 *   <li>사용자는 자신의 자격증만 수정할 수 있습니다.</li>
 *   <li>파일이 주어질 경우 기존 파일은 삭제되고 새 파일로 교체됩니다.</li>
 * </ul>
 * @author snowykte0426
 */
public interface UpdateCurrentCertificateUseCase {
    void execute(Long id, String name, LocalDate acquisitionDate, MultipartFile file);
}