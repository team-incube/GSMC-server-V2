package team.incube.gsmc.v2.domain.member.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.incube.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incube.gsmc.v2.domain.member.application.usecase.SearchStudentUseCase;
import team.incube.gsmc.v2.domain.member.domain.StudentDetailWithEvidence;
import team.incube.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;
import team.incube.gsmc.v2.domain.member.presentation.data.response.SearchStudentResponse;

/**
 * 학생 조건 검색 유스케이스의 구현 클래스입니다.
 * <p>{@link SearchStudentUseCase}를 구현하며, 이름, 학년, 반 번호를 기준으로 필터링된 학생 목록을 페이징 처리하여 조회합니다.
 * <p>{@link StudentDetailPersistencePort}를 통해 영속성 계층에서 검색 쿼리를 수행하고,
 * 결과는 {@link SearchStudentResponse}로 매핑되어 클라이언트에 전달됩니다.
 * <p>검색 결과에는 학생의 점수, 증빙자료 상태, 권한 정보 등이 포함됩니다.
 * @author snowykte0426
 */
@Service
@RequiredArgsConstructor
public class SearchStudentService implements SearchStudentUseCase {

    private final StudentDetailPersistencePort studentDetailPersistencePort;

    @Override
    public SearchStudentResponse execute(String name, Integer grade, Integer classNumber, Integer page, Integer size) {
        Page<StudentDetailWithEvidence> studentDetails = studentDetailPersistencePort.searchStudentDetailWithEvidenceReviewStatusNotNullMember(name, grade, classNumber, Pageable.ofSize(size));
        return new SearchStudentResponse(
                studentDetails.getTotalPages(),
                studentDetails.getTotalElements(),
                studentDetails.stream().map(
                        studentDetail -> new GetStudentResponse(
                                studentDetail.getStudentDetail().getMember().getEmail(),
                                studentDetail.getStudentDetail().getMember().getName(),
                                studentDetail.getStudentDetail().getGrade(),
                                studentDetail.getStudentDetail().getClassNumber(),
                                studentDetail.getStudentDetail().getNumber(),
                                studentDetail.getStudentDetail().getTotalScore(),
                                studentDetail.getHasPendingEvidence(),
                                studentDetail.getStudentDetail().getMember().getRole()
                        )).toList()
        );
    }
}