package team.incude.gsmc.v2.domain.certificate.application.usecase;

/**
 * 자격증 삭제 기능을 제공하는 인터페이스입니다.
 * <p>자격증 ID 또는 학생 코드와 자격증 ID를 기반으로 자격증을 삭제합니다.
 * <p>주요 기능:
 * <ul>
 *   <li>자격증 ID로 자격증 삭제</li>
 *   <li>학생 코드와 자격증 ID로 자격증 삭제</li>
 * </ul>
 * @author snowykte0426
 */
public interface DeleteCertificateUseCase {
    void execute(Long id);

    void execute(String studentCode, Long id);
}