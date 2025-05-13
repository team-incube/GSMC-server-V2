package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.certificate.application.port.CertificatePersistencePort;
import team.incude.gsmc.v2.domain.certificate.application.usecase.DeleteCertificateUseCase;
import team.incude.gsmc.v2.domain.certificate.domain.Certificate;
import team.incude.gsmc.v2.domain.certificate.exception.CertificateNotBelongToMemberException;
import team.incude.gsmc.v2.domain.evidence.application.port.EvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.OtherEvidencePersistencePort;
import team.incude.gsmc.v2.domain.evidence.application.port.S3Port;
import team.incude.gsmc.v2.domain.evidence.domain.OtherEvidence;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.score.exception.InvalidScoreValueException;
import team.incude.gsmc.v2.global.event.ScoreUpdatedEvent;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;
import team.incude.gsmc.v2.global.util.ExtractFileKeyUtil;

/**
 * 자격증 삭제 유스케이스를 구현한 서비스 클래스입니다.
 * <p>{@link DeleteCertificateUseCase}를 구현하며, 현재 사용자 또는 특정 사용자의 자격증을 삭제합니다.
 * <p>삭제 시 관련된 S3 파일, Evidence, OtherEvidence, Score 정보도 함께 처리됩니다.
 * <ul>
 *   <li>자격증 소유자 검증</li>
 *   <li>S3 파일 삭제</li>
 *   <li>Score 차감 및 저장</li>
 *   <li>Evidence 및 자격증 삭제</li>
 * </ul>
 * @author snowykte0426
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeleteCertificateService implements DeleteCertificateUseCase {

    private final ScorePersistencePort scorePersistencePort;
    private final CertificatePersistencePort certificatePersistencePort;
    private final EvidencePersistencePort evidencePersistencePort;
    private final OtherEvidencePersistencePort otherEvidencePersistencePort;
    private final MemberPersistencePort memberPersistencePort;
    private final S3Port s3Port;
    private final CurrentMemberProvider currentMemberProvider;
    private final ApplicationContext applicationContext;

    private static final String CATEGORY_NAME = "MAJOR-CERTIFICATE_NUM";
    private static final String HUMANITIES_CERTIFICATE_KOREAN_HISTORY_CATEGORY_NAME = "HUMANITIES-CERTIFICATE-KOREAN_HISTORY";
    private static final String HUMANITIES_CERTIFICATE_CHINESE_CHARACTER_CATEGORY_NAME = "HUMANITIES-CERTIFICATE-CHINESE_CHARACTER";

    /**
     * 현재 로그인한 사용자의 자격증을 삭제합니다.
     * @param id 삭제할 자격증의 ID
     */
    @Override
    public void execute(Long id) {
        deleteCertificate(currentMemberProvider.getCurrentUser(), id);
    }

    /**
     * 특정 사용자의 자격증을 삭제합니다.
     * @param studentCode 자격증을 삭제할 사용자의 학생 코드
     * @param id 삭제할 자격증의 ID
     */
    @Override
    public void execute(String studentCode, Long id) {
        Member member = memberPersistencePort.findMemberByStudentDetailStudentCode(studentCode);
        deleteCertificate(member, id);
        applicationContext.publishEvent(new ScoreUpdatedEvent(studentCode));
    }

    /**
     * 주어진 사용자와 자격증 ID를 기반으로 자격증과 관련된 모든 리소스를 삭제합니다.
     * <p>자격증 소유자 검증, S3 파일 삭제, 점수 차감 및 저장, 자격증 및 증빙 삭제 등의 처리를 포함합니다.
     * @param member 자격증 소유자
     * @param id 자격증 ID
     * @throws CertificateNotBelongToMemberException 자격증이 해당 사용자 소유가 아닌 경우
     * @throws InvalidScoreValueException 점수가 0 이하인 경우
     */
    private void deleteCertificate(Member member, Long id) {
        Certificate certificate = certificatePersistencePort.findCertificateByIdWithLock(id);
        if (!certificate.getMember().getId().equals(member.getId())) {
            throw new CertificateNotBelongToMemberException();
        }

        OtherEvidence otherEvidence = certificate.getEvidence();
        s3Port.deleteFile(ExtractFileKeyUtil.extractFileKey(otherEvidence.getFileUri()));

        Score score = scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock(
                certificate.getName().startsWith("한국사 능력검정")
                        ? HUMANITIES_CERTIFICATE_KOREAN_HISTORY_CATEGORY_NAME
                        : certificate.getName().startsWith("한자검정시험")
                        ? HUMANITIES_CERTIFICATE_CHINESE_CHARACTER_CATEGORY_NAME
                        : CATEGORY_NAME,
                member.getEmail()
        );
        if (score.getValue() > 0) {
            score.minusValue(1);
            scorePersistencePort.saveScore(score);
        } else {
            throw new InvalidScoreValueException();
        }

        certificatePersistencePort.deleteCertificateById(certificate.getId());
        otherEvidencePersistencePort.deleteOtherEvidenceById(otherEvidence.getId().getId());
        evidencePersistencePort.deleteEvidenceById(otherEvidence.getId().getId());
    }
}