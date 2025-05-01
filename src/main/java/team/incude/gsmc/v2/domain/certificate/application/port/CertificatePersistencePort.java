package team.incude.gsmc.v2.domain.certificate.application.port;

import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.List;

/**
 * 자격증 도메인의 영속성 계층과의 통신을 담당하는 Port 인터페이스입니다.
 * <p>해당 인터페이스는 Repository 또는 외부 저장소와의 연동 책임을 가지며,
 * Adapter 계층에서 구현되어 도메인 계층과의 의존성을 분리합니다.
 * <p>{@code @Port(direction = PortDirection.OUTBOUND)}로 정의되며,
 * 도메인 로직에서 외부 저장소에 접근할 때 사용됩니다.
 * @author snowykte0426
 */
@Port(direction = PortDirection.OUTBOUND)
public interface CertificatePersistencePort {
    /**
     * 지정된 ID에 해당하는 자격증을 조회하고, 비관적 락(Pessimistic Lock)을 걸어 반환합니다.
     * <p>동시성 문제를 방지하기 위해 자격증 수정 또는 삭제 전 반드시 호출되어야 합니다.
     * 트랜잭션 내에서 호출 시 해당 자격증에 대한 수정/삭제가 동시에 일어나지 않도록 보장합니다.
     * @param id 자격증의 고유 ID
     * @return 락이 적용된 자격증 엔티티
     */
    Certificate findCertificateByIdWithLock(Long id);

    /**
     * 주어진 학생 코드에 해당하는 자격증 목록을 조회합니다.
     * @param studentCode 학생의 고유 코드
     * @return 해당 학생이 소유한 자격증 목록
     */
    List<Certificate> findCertificateByStudentDetailStudentCode(String studentCode);

    /**
     * 자격증 정보를 저장합니다.
     * <p>신규 자격증 등록뿐 아니라 수정된 자격증 정보를 반영할 때도 사용됩니다.
     * @param certificate 저장할 자격증 엔티티
     * @return 저장된 자격증 엔티티
     */
    Certificate saveCertificate(Certificate certificate);

    /**
     * 지정된 ID에 해당하는 자격증을 삭제합니다.
     * @param id 삭제할 자격증의 고유 ID
     */
    void deleteCertificateById(Long id);
}