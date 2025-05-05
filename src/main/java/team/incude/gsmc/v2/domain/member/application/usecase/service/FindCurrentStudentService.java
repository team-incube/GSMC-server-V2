package team.incude.gsmc.v2.domain.member.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.application.usecase.FindCurrentStudentUseCase;
import team.incude.gsmc.v2.domain.member.domain.StudentDetailWithEvidence;
import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;
import team.incude.gsmc.v2.global.security.jwt.usecase.service.CurrentMemberProvider;

/**
 * 현재 로그인한 학생 정보를 조회하는 유스케이스 구현 클래스입니다.
 * <p>{@link FindCurrentStudentUseCase}를 구현하며, 인증된 사용자의 이메일을 기준으로 학생 상세 정보와 증빙자료 상태를 함께 조회합니다.
 * <p>반환되는 정보는 {@link GetStudentResponse}에 담겨 클라이언트에 전달됩니다.
 * <p>이 서비스는 {@link CurrentMemberProvider}를 통해 로그인된 사용자 정보를 획득하고,
 * {@link StudentDetailPersistencePort}를 통해 학생 정보를 영속성 계층에서 조회합니다.
 * @author snowykte0426
 */
@Service
@RequiredArgsConstructor
public class FindCurrentStudentService implements FindCurrentStudentUseCase {

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