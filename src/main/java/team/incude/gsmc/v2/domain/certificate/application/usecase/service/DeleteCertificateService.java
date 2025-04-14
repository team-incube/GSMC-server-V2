package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import team.incude.gsmc.v2.global.util.ExtractFileKeyUtil;

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

    private static final String CATEGORY_NAME = "MAJOR-CERTIFICATE-NUM";

    // TODO: auth 구현 전 임시 코드
    private void setSecurityContext(String email) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, "");
        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // TODO: auth 구현 전 임시 코드
    private String getAuthenticatedEmail() {
        setSecurityContext("s24058@gsm.hs.kr");
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public void execute(Long id) {
        String email = getAuthenticatedEmail();
        Member member = memberPersistencePort.findMemberByEmail(email);
        deleteCertificate(member, id);
    }

    @Override
    public void execute(String email, Long id) {
        Member member = memberPersistencePort.findMemberByEmail(email);
        deleteCertificate(member, id);
    }

    private void deleteCertificate(Member member, Long id) {
        Certificate certificate = certificatePersistencePort.findCertificateByIdWithLock(id);
        if (!certificate.getMember().getId().equals(member.getId())) {
            throw new CertificateNotBelongToMemberException();
        }

        OtherEvidence otherEvidence = certificate.getEvidence();
        s3Port.deleteFile(ExtractFileKeyUtil.extractFileKey(otherEvidence.getFileUri()));

        Score score = scorePersistencePort.findScoreByCategoryNameAndMemberEmailWithLock(CATEGORY_NAME, member.getEmail());
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