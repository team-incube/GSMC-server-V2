package team.incude.gsmc.v2.domain.member.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.application.usecase.FindCurrentStudentUseCase;
import team.incude.gsmc.v2.domain.member.domain.StudentDetailWithEvidence;
import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

@Service
@RequiredArgsConstructor
public class FindCurrentCurrentStudentService implements FindCurrentStudentUseCase {

    private final StudentDetailPersistencePort studentDetailPersistencePort;
    private final CurrentMemberProvider currentMemberProvider;

    @Override
    public GetStudentResponse execute() {
        StudentDetailWithEvidence studentDetailWithEvidence = studentDetailPersistencePort.findStudentDetailWithEvidenceByMemberEmail(currentMemberProvider.getCurrentUser().getEmail());
        return new GetStudentResponse(
                studentDetailWithEvidence.getStudentDetail().getMember().getEmail(),
                studentDetailWithEvidence.getStudentDetail().getMember().getName(),
                studentDetailWithEvidence.getStudentDetail().getGrade(),
                studentDetailWithEvidence.getStudentDetail().getClassNumber(),
                studentDetailWithEvidence.getStudentDetail().getNumber(),
                studentDetailWithEvidence.getStudentDetail().getTotalScore(),
                studentDetailWithEvidence.getHasPendingEvidence(),
                studentDetailWithEvidence.getStudentDetail().getMember().getRole()
        );
    }
}