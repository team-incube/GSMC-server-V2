package team.incube.gsmc.v2.domain.certificate.application;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import team.incube.gsmc.v2.domain.certificate.application.port.CertificateApplicationPort;
import team.incube.gsmc.v2.domain.certificate.application.usecase.CreateCertificateUseCase;
import team.incube.gsmc.v2.domain.certificate.application.usecase.DeleteCertificateUseCase;
import team.incube.gsmc.v2.domain.certificate.application.usecase.FindCertificateUseCase;
import team.incube.gsmc.v2.domain.certificate.application.usecase.UpdateCurrentCertificateUseCase;
import team.incube.gsmc.v2.domain.certificate.persentation.data.response.GetCertificateResponse;
import team.incube.gsmc.v2.global.annotation.PortDirection;
import team.incube.gsmc.v2.global.annotation.adapter.Adapter;

import java.time.LocalDate;

/**
 * {@link CertificateApplicationPort}의 구현체로, 유스케이스를 호출하여 자격증 관련 애플리케이션 로직을 실행합니다.
 * <p>{@code @Adapter(direction = PortDirection.INBOUND)}로 선언되어 어댑터 계층에서 애플리케이션 계층으로의 진입점을 제공합니다.
 * 각 메서드는 포트를 통해 정의된 기능을 실제로 수행하는 유스케이스로 위임합니다.
 * <p>역할:
 * <ul>
 *   <li>현재 사용자 또는 특정 학생의 자격증 목록 조회</li>
 *   <li>자격증 생성</li>
 *   <li>현재 사용자의 자격증 수정</li>
 *   <li>자격증 삭제</li>
 * </ul>
 * @author snowykte0426
 */
@Adapter(direction = PortDirection.INBOUND)
@RequiredArgsConstructor
public class CertificateApplicationAdapter implements CertificateApplicationPort {

    private final FindCertificateUseCase findCertificateUseCase;
    private final CreateCertificateUseCase createCertificateUseCase;
    private final UpdateCurrentCertificateUseCase updateCurrentCertificateUseCase;
    private final DeleteCertificateUseCase deleteCertificateUseCase;

    /**
     * 현재 로그인한 사용자의 자격증 목록을 조회합니다.
     * @return 현재 사용자의 자격증 목록
     */
    @Override
    public GetCertificateResponse findCurrentCertificate() {
        return findCertificateUseCase.execute();
    }

    /**
     * 특정 학생 코드(이메일)에 해당하는 자격증 목록을 조회합니다.
     * @param email 학생의 이메일 또는 식별자
     * @return 해당 학생의 자격증 목록
     */
    @Override
    public GetCertificateResponse findCertificateByStudentCode(String email) {
        return findCertificateUseCase.execute(email);
    }

    /**
     * 자격증을 새로 등록합니다.
     * @param name 자격증 이름
     * @param acquisitionDate 자격증 취득일
     * @param file 자격증 파일 (이미지 또는 PDF)
     */
    @Override
    public void createCertificate(String name, LocalDate acquisitionDate, MultipartFile file) {
        createCertificateUseCase.execute(name, acquisitionDate, file);
    }

    /**
     * 현재 로그인한 사용자의 자격증 정보를 수정합니다.
     * @param id 자격증 고유 ID
     * @param name 변경할 자격증 이름
     * @param acquisitionDate 변경할 취득일
     * @param file 새로 업로드할 자격증 파일
     */
    @Override
    public void updateCurrentCertificate(Long id, String name, LocalDate acquisitionDate, MultipartFile file) {
        updateCurrentCertificateUseCase.execute(id, name, acquisitionDate, file);
    }

    /**
     * 현재 로그인한 사용자의 자격증을 삭제합니다.
     * @param id 삭제할 자격증의 ID
     */
    @Override
    public void deleteCurrentCertificate(Long id) {
        deleteCertificateUseCase.execute(id);
    }

    /**
     * 이메일과 자격증 ID를 기반으로 자격증을 삭제합니다.
     * @param email 학생의 이메일
     * @param id 삭제할 자격증의 ID
     */
    @Override
    public void deleteCertificateByStudentCodeAndId(String email, Long id) {
        deleteCertificateUseCase.execute(email, id);
    }
}