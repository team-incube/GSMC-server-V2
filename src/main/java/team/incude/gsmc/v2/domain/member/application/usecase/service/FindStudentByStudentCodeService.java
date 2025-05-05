package team.incude.gsmc.v2.domain.member.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.application.usecase.FindStudentByStudentCodeUseCase;
import team.incude.gsmc.v2.domain.member.domain.StudentDetailWithEvidence;
import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;

/**
 * 학생 코드 기반으로 학생 정보를 조회하는 유스케이스 구현 클래스입니다.
 * <p>{@link FindStudentByStudentCodeUseCase}를 구현하며, 주어진 학생 코드에 해당하는 학생 정보를 조회하여 {@link GetStudentResponse}로 반환합니다.
 * <p>{@link StudentDetailPersistencePort}를 통해 학생 상세 정보와 증빙 상태를 함께 조회합니다.
 * @author snowykte0426
 */
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