package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.application.usecase.UpdateCurrentCertificateUseCase;
import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.domain.certificate.exception.CertificateCategoryMismatchException;
import team.incude.gsmc.v2.domain.certificate.exception.CertificateNotBelongToMemberException;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.domain.Evidence;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.evidence.domain.constant.EvidenceType;
import team.incude.gsmc.v2.domain.evidence.domain.constant.ReviewStatus;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.global.security.jwt.application.usecase.service.CurrentMemberProvider;
import team.incude.gsmc.v2.global.thirdparty.aws.exception.S3UploadFailedException;
import team.incude.gsmc.v2.global.util.ExtractFileKeyUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 현재 로그인한 사용자의 자격증 정보를 수정하는 서비스입니다.
 * <p>{@link UpdateCurrentCertificateUseCase}를 구현하며, 인증된 사용자가 자신의 자격증 정보를 변경할 수 있도록 처리합니다.
 * <p>변경 가능한 항목: 자격증 이름, 취득일, 증빙 파일(S3 업로드 포함)
 * <ul>
 *   <li>기존 자격증 소유자 검증</li>
 *   <li>S3 파일 교체 및 Evidence 새로 생성</li>
 *   <li>자격증 정보 업데이트 및 저장</li>
 * </ul>
 * <p>변경 사항이 없을 경우 기존 값을 그대로 유지하며, 파일이 업로드되지 않은 경우 기존 Evidence를 재사용합니다.
 *
 * @author snowykte0426
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateCurrentCertificateService implements UpdateCurrentCertificateUseCase {

    private final CertificatePersistencePort certificatePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final MemberPersistencePort memberPersistencePort;
    private final S3Port s3Port;
    private final CurrentMemberProvider currentMemberProvider;

    /**
     * 자격증 정보를 업데이트합니다.
     * <p>현재 로그인한 사용자의 자격증을 찾아 이름, 취득일, 증빙 파일 등을 변경합니다.
     * 파일이 새로 업로드된 경우 S3에서 기존 파일을 삭제하고 새 파일을 업로드합니다.
     * @param certificateId   수정할 자격증의 ID
     * @param name            변경할 자격증 이름 (null 허용)
     * @param acquisitionDate 변경할 자격증 취득일 (null 허용)
     * @param file            교체할 자격증 파일 (null 허용)
     * @throws CertificateNotBelongToMemberException 자격증이 현재 사용자 소유가 아닐 경우
     */
    @Override
    public void execute(Long certificateId, String name, LocalDate acquisitionDate, MultipartFile file) {
        Member member = memberPersistencePort.findMemberByEmail(currentMemberProvider.getCurrentUser().getEmail());

        Certificate existingCertificate = certificatePersistencePort.findCertificateByIdWithLock(certificateId);
        validateCategoryChange(existingCertificate.getName(), name);
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

    /**
     * 자격증 이름 변경 시 한자 및 한국사 카테고리의 일관성을 검증합니다.
     * <p>예를 들어, 일반 자격증을 한자/한국사 자격증으로 변경하거나 그 반대의 경우 예외를 발생시킵니다.
     * <p>단, 동일한 카테고리 내에서 세부 등급 변경(예: 1급 → 2급)은 허용됩니다.
     * @param originalName 기존 자격증 이름
     * @param newName      새 자격증 이름 (null 가능)
     * @throws CertificateCategoryMismatchException 유효하지 않은 카테고리 변경 시 발생
     */
    private void validateCategoryChange(String originalName, String newName) {
        boolean isOriginalHanja = originalName != null && originalName.startsWith("한자검정시험");
        boolean isOriginalHistory = originalName != null && originalName.startsWith("한국사 능력검정");
        boolean isNewHanja = newName != null && newName.startsWith("한자검정시험");
        boolean isNewHistory = newName != null && newName.startsWith("한국사 능력검정");

        if ((isOriginalHanja && !isNewHanja) ||
            (!isOriginalHanja && isNewHanja) ||
            (isOriginalHistory && !isNewHistory) ||
            (!isOriginalHistory && isNewHistory)) {
            throw new CertificateCategoryMismatchException();
        }
    }

    /**
     * 기존 파일을 S3에서 삭제합니다.
     * @param fileUri 삭제할 파일의 URI
     */
    private void deleteExistingFile(String fileUri) {
        s3Port.deleteFile(ExtractFileKeyUtil.extractFileKey(fileUri));
    }

    /**
     * 새 파일을 S3에 업로드합니다.
     * @param file 업로드할 파일
     * @return 업로드된 파일의 URI
     */
    private String uploadFileToS3(MultipartFile file) {
        try {
            return s3Port.uploadFile(file.getOriginalFilename(), file.getInputStream()).join();
        } catch (IOException e) {
            throw new S3UploadFailedException();
        }
    }
}