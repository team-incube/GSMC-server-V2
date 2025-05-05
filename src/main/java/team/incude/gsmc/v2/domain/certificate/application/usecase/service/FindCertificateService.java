package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.application.usecase.FindCertificateUseCase;
import team.incude.gsmc.v2.domain.certificate.persentation.data.GetCertificateDto;
import team.incude.gsmc.v2.domain.certificate.persentation.data.response.GetCertificateResponse;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

/**
 * 자격증 조회 기능을 제공하는 서비스 클래스입니다.
 * <p>{@link FindCertificateUseCase}를 구현하며, 현재 로그인한 사용자 또는 특정 학생의 자격증 목록을 조회합니다.
 * <p>주요 기능:
 * <ul>
 *   <li>현재 사용자의 이메일을 기준으로 학생 코드를 조회하고 자격증 목록 반환</li>
 *   <li>학생 코드를 직접 받아 해당 자격증 목록 반환</li>
 * </ul>
 * <p>각 자격증은 {@link GetCertificateDto}로 변환되어 {@link GetCertificateResponse}에 담겨 반환됩니다.
 * @author snowykte0426
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindCertificateService implements FindCertificateUseCase {

    private final CertificatePersistencePort certificatePersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    /**
     * 현재 로그인한 사용자의 이메일을 기준으로 자격증 목록을 조회합니다.
     * @return 현재 사용자의 자격증 목록 응답 객체
     */
    @Override
    public GetCertificateResponse execute() {
        return findCertificate(studentDetailPersistencePort.findStudentDetailByMemberEmail(currentMemberProvider.getCurrentUser().getEmail()).getStudentCode());
    }

    /**
     * 주어진 학생 코드에 해당하는 자격증 목록을 조회합니다.
     * @param studentCode 자격증을 조회할 학생의 고유 코드
     * @return 해당 학생의 자격증 목록 응답 객체
     */
    @Override
    public GetCertificateResponse execute(String studentCode) {
        return findCertificate(studentCode);
    }

    /**
     * 학생 코드 기반으로 자격증 목록을 조회하고 DTO로 변환합니다.
     * @param studentCode 자격증을 조회할 학생의 고유 코드
     * @return {@link GetCertificateDto} 리스트를 포함한 응답 객체
     */
    private GetCertificateResponse findCertificate(String studentCode) {
        return new GetCertificateResponse(certificatePersistencePort.findCertificateByStudentDetailStudentCode(studentCode)
                .stream()
                .map(certificate -> new GetCertificateDto(
                        certificate.getId(),
                        certificate.getName(),
                        certificate.getAcquisitionDate().toString(),
                        certificate.getEvidence().getFileUri()
                )).toList());
    }
}