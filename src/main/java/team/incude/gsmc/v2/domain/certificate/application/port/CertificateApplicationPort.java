package team.incude.gsmc.v2.domain.certificate.application.port;

import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificateResponse;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.time.LocalDate;


/**
 * 자격증 도메인의 애플리케이션 계층 Port 인터페이스입니다.
 * <p>인증된 사용자의 자격증 생성, 수정, 삭제 및 조회와 관련된 유스케이스를 정의합니다.
 * 해당 인터페이스는 {@code @Port(direction = PortDirection.INBOUND)}로 선언되어 있으며,
 * 비즈니스 로직을 외부 계층(Web 등)에서 호출하기 위한 진입점 역할을 합니다.
 * <ul>
 *   <li>{@code findCurrentCertificate()} - 현재 로그인한 사용자의 자격증 목록 조회</li>
 *   <li>{@code findCertificateByStudentCode(String)} - 특정 학생의 자격증 목록 조회</li>
 *   <li>{@code createCertificate(...)} - 자격증 생성</li>
 *   <li>{@code updateCurrentCertificate(...)} - 자격증 수정</li>
 *   <li>{@code deleteCurrentCertificate(Long)} - 현재 사용자의 자격증 삭제</li>
 *   <li>{@code deleteCertificateByStudentCodeAndId(String, Long)} - 특정 학생의 자격증 삭제</li>
 * </ul>
 * 각 메서드는 애플리케이션 계층에서 도메인 로직을 캡슐화하고 어댑터 계층(Web 등)과 도메인 계층 사이의 중간 연결점을 제공합니다.
 * @author snowykte0426
 */
@Port(direction = PortDirection.INBOUND)
public interface CertificateApplicationPort {
    /**
     * 현재 로그인한 사용자의 자격증 목록을 조회합니다.
     * @return 현재 사용자에게 등록된 자격증 목록
     */
    GetCertificateResponse findCurrentCertificate();

    /**
     * 특정 학생 코드에 해당하는 자격증 목록을 조회합니다.
     * @param studentCode 조회할 학생의 고유 코드
     * @return 해당 학생의 자격증 목록
     */
    GetCertificateResponse findCertificateByStudentCode(String studentCode);

    /**
     * 현재 로그인한 사용자에 대해 자격증을 등록합니다.
     * @param name 자격증 이름
     * @param acquisitionDate 자격증 취득일
     * @param file 자격증 파일 (이미지 또는 PDF)
     */
    void createCertificate(String name, LocalDate acquisitionDate, MultipartFile file);

    /**
     * 현재 로그인한 사용자의 자격증을 수정합니다.
     * @param id 수정할 자격증의 고유 ID
     * @param name 변경할 자격증 이름
     * @param acquisitionDate 변경할 자격증 취득일
     * @param file 새롭게 업로드할 자격증 파일
     */
    void updateCurrentCertificate(Long id, String name, LocalDate acquisitionDate, MultipartFile file);

    /**
     * 현재 로그인한 사용자의 자격증을 삭제합니다.
     * @param id 삭제할 자격증의 고유 ID
     */
    void deleteCurrentCertificate(Long id);

    /**
     * 특정 학생 코드와 자격증 ID에 해당하는 자격증을 삭제합니다.
     * @param studentCode 학생의 고유 코드
     * @param id 삭제할 자격증의 고유 ID
     */
    void deleteCertificateByStudentCodeAndId(String studentCode, Long id);
}