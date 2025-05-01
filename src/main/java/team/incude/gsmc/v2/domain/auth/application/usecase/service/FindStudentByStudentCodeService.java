package team.incude.gsmc.v2.domain.member.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.application.usecase.FindStudentByStudentCodeUseCase;
import team.incude.gsmc.v2.domain.member.domain.StudentDetailWithEvidence;
import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;

@Service
@RequiredArgsConstructor
public class FindStudentByStudentCodeService implements FindStudentByStudentCodeUseCase {

    private final StudentDetailPersistencePort studentDetailPersistencePort;

    @Override
    public GetStudentResponse execute(String studentCode) {
        StudentDetailWithEvidence studentDetailWithEvidence = studentDetailPersistencePort.findStudentDetailWithEvidenceByStudentCode(studentCode);
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