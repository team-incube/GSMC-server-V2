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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindCertificateService implements FindCertificateUseCase {

    private final CertificatePersistencePort certificatePersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    @Override
    public GetCertificateResponse execute() {
        return findCertificate(studentDetailPersistencePort.findStudentDetailByMemberEmail(currentMemberProvider.getCurrentUser().getEmail()).getStudentCode());
    }

    @Override
    public GetCertificateResponse execute(String studentCode) {
        return findCertificate(studentCode);
    }

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