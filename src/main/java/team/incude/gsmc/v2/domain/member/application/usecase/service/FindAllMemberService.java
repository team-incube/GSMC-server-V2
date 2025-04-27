package team.incude.gsmc.v2.domain.member.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.member.application.usecase.FindAllMemberUseCase;
import team.incude.gsmc.v2.domain.member.persistence.MemberPersistenceAdapter;
import team.incude.gsmc.v2.domain.member.persistence.StudentDetailPersistenceAdapter;
import team.incude.gsmc.v2.domain.member.presentation.data.response.GetMemberResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllMemberService implements FindAllMemberUseCase {

    private final StudentDetailPersistenceAdapter studentDetailPersistenceAdapter;

    @Override
    public List<GetMemberResponse> getAllMembers() {
        return studentDetailPersistenceAdapter.findStudentDetailNotNullMember()
                .stream()
                .map(studentDetail -> new GetMemberResponse(
                        studentDetail.getMember().getEmail(),
                        studentDetail.getMember().getName(),
                        studentDetail.getGrade(),
                        studentDetail.getClassNumber(),
                        studentDetail.getNumber(),
                        studentDetail.getTotalScore(),
                        studentDetail.getMember().getRole()
                ))
                .toList();
    }
}