package team.incube.gsmc.v2.domain.member.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incube.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incube.gsmc.v2.domain.member.application.usecase.FindAllStudentUseCase;
import team.incube.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;

import java.util.List;

/**
 * 전체 학생 목록을 조회하는 유스케이스 구현 클래스입니다.
 * <p>{@link FindAllStudentUseCase}를 구현하며, 학생 정보를 조회하는 기능을 제공합니다.
 * <p>{@link StudentDetailPersistencePort}를 통해 영속성 계층에서 학생 정보를 조회하고,
 * {@link GetStudentResponse}로 매핑하여 클라이언트에 전달할 수 있는 형태로 반환합니다.
 * @author snowykte0426
 */
@Service
@RequiredArgsConstructor
public class FindAllStudentService implements FindAllStudentUseCase {

    private final StudentDetailPersistencePort studentDetailPersistencePort;

    @Override
    public List<GetStudentResponse> execute() {
        return studentDetailPersistencePort.findStudentDetailWithEvidenceReviewStatusNotNullMember()
                .stream()
                .map(studentDetail -> new GetStudentResponse(
                        studentDetail.getStudentDetail().getMember().getEmail(),
                        studentDetail.getStudentDetail().getMember().getName(),
                        studentDetail.getStudentDetail().getGrade(),
                        studentDetail.getStudentDetail().getClassNumber(),
                        studentDetail.getStudentDetail().getNumber(),
                        studentDetail.getStudentDetail().getTotalScore(),
                        studentDetail.getHasPendingEvidence(),
                        studentDetail.getStudentDetail().getMember().getRole()
                ))
                .toList();
    }
}