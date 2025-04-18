package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.application.usecase.UpdateCurrentCertificateUseCase;
import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.domain.certificate.exception.CertificateNotBelongToMemberException;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;
import team.incude.gsmc.v2.global.util.ExtractFileKeyUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateCurrentCertificateService implements UpdateCurrentCertificateUseCase {

    private final CertificatePersistencePort certificatePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final MemberPersistencePort memberPersistencePort;
    private final S3Port s3Port;
    private final CurrentMemberProvider currentMemberProvider;

    @Override
    public void execute(Long certificateId, String name, LocalDate acquisitionDate, MultipartFile file) {
        Member member = memberPersistencePort.findMemberByEmail(currentMemberProvider.getCurrentUser().getEmail());

        Certificate existingCertificate = certificatePersistencePort.findCertificateByIdWithLock(certificateId);

        OtherEvidence existingOtherEvidence = existingCertificate.getEvidence();
        String existingFileUri = existingOtherEvidence.getFileUri();
        Evidence evidence = existingOtherEvidence.getId();

        if (!existingCertificate.getMember().getId().equals(member.getId())) {
            throw new CertificateNotBelongToMemberException();
        }

        OtherEvidence finalOtherEvidence = existingOtherEvidence;

        if (file != null) {
            deleteExistingFile(existingFileUri);

            String newFileUri = uploadFileToS3(file);

            Evidence newEvidence = Evidence.builder()
                    .score(existingOtherEvidence.getId().getScore())
                    .evidenceType(EvidenceType.CERTIFICATE)
                    .reviewStatus(ReviewStatus.PENDING)
                    .createdAt(evidence.getCreatedAt())
                    .updatedAt(LocalDateTime.now())
                    .build();

            finalOtherEvidence = otherEvidencePersistencePort.saveOtherEvidence(
                    OtherEvidence.builder()
                            .id(newEvidence)
                            .fileUri(newFileUri)
                            .build()
            );
        }

        Certificate updatedCertificate = Certificate.builder()
                .id(existingCertificate.getId())
                .member(existingCertificate.getMember())
                .name(name != null ? name : existingCertificate.getName())
                .acquisitionDate(acquisitionDate != null ? acquisitionDate : existingCertificate.getAcquisitionDate())
                .evidence(finalOtherEvidence)
                .build();

        certificatePersistencePort.saveCertificate(updatedCertificate);
    }

    private void deleteExistingFile(String fileUri) {
        String fileName = ExtractFileKeyUtil.extractFileKey(fileUri);
        s3Port.deleteFile(fileName);
    }


    private String uploadFileToS3(MultipartFile file) {
        try {
            return s3Port.uploadFile(file.getOriginalFilename(), file.getInputStream()).join();
        } catch (IOException e) {
            throw new S3UploadFailedException();
        }
    }
}