package team.incude.gsmc.v2.domain.member.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.member.application.usecase.FindAllStudentUseCase;
import team.incude.gsmc.v2.domain.member.persistence.StudentDetailPersistenceAdapter;
import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllStudentService implements FindAllStudentUseCase {

    private final StudentDetailPersistenceAdapter studentDetailPersistenceAdapter;

    @Override
    public List<GetStudentResponse> getAllStudents() {
        return studentDetailPersistenceAdapter.findStudentDetailNotNullMember()
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