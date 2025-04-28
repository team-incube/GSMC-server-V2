package team.incude.gsmc.v2.domain.member.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.member.application.usecase.SearchStudentUseCase;
import team.incude.gsmc.v2.domain.member.domain.StudentDetailWithEvidence;
import team.incude.gsmc.v2.domain.member.persistence.StudentDetailPersistenceAdapter;
import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;
import team.incude.gsmc.v2.domain.member.presentation.data.response.SearchStudentResponse;

@Service
@RequiredArgsConstructor
public class SearchStudentService implements SearchStudentUseCase {

    private final StudentDetailPersistenceAdapter studentDetailPersistenceAdapter;

    @Override
    public SearchStudentResponse searchStudents(String name, Integer grade, Integer classNumber, Integer page, Integer size) {
        Page<StudentDetailWithEvidence> studentDetails = studentDetailPersistenceAdapter.searchStudentDetailWithEvidenceReiewStatusNotNullMember(name, grade, classNumber, Pageable.ofSize(size));
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