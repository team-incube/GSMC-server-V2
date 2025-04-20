package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incude.gsmc.v2.domain.auth.application.port.AuthenticationPersistencePort;
import team.incude.gsmc.v2.domain.auth.application.usecase.SignUpUseCase;
import team.incude.gsmc.v2.domain.auth.domain.Authentication;
import team.incude.gsmc.v2.domain.auth.exception.EmailFormatInvalidException;
import team.incude.gsmc.v2.domain.auth.exception.MemberExistException;
import team.incude.gsmc.v2.domain.auth.exception.MemberForbiddenException;
import team.incude.gsmc.v2.domain.member.application.port.MemberPersistencePort;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.Member;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.member.domain.constant.MemberRole;
import team.incude.gsmc.v2.domain.member.persistence.mapper.MemberMapper;
import team.incude.gsmc.v2.domain.member.persistence.mapper.StudentDetailMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class SignUpService implements SignUpUseCase {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberPersistencePort memberPersistencePort;
    private final AuthenticationPersistencePort authenticationPersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;

    public void execute(String name, String email, String password) {

        Authentication authentication = authenticationPersistencePort.findAuthenticationByEmail(email);
        if (authentication == null || Boolean.FALSE.equals(authentication.getVerified())) {
            throw new MemberForbiddenException();
        }
        if (memberPersistencePort.existsMemberByEmail(email)) {
            throw new MemberExistException();
        }

       StudentDetail existedStudentDetail = studentDetailPersistencePort.findStudentDetailByStudentCode(parsingEmail(email));

        Member member = Member.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode(password))
                .role(MemberRole.ROLE_STUDENT)
                .build();

        Member savedMember = memberPersistencePort.saveMember(member);

        StudentDetail studentDetail = StudentDetail.builder()
                .id(existedStudentDetail.getId())
                .member(savedMember)
                .grade(existedStudentDetail.getGrade())
                .classNumber(existedStudentDetail.getClassNumber())
                .number(existedStudentDetail.getNumber())
                .totalScore(existedStudentDetail.getTotalScore())
                .studentCode(existedStudentDetail.getStudentCode())
                .build();

        studentDetailPersistencePort.saveStudentDetail(studentDetail);
    }

    private String parsingEmail(String email) {

        if (!email.startsWith("s") || !email.endsWith("@gsm.hs.kr")) {
            throw new EmailFormatInvalidException();
        }

        String studentId = email.substring(1, email.indexOf("@"));
        try {
            //Integer studentCode = Integer.parseInt(studentId);
            //return studentCode;
            return studentId;
        } catch (NumberFormatException e) {
            throw new EmailFormatInvalidException();
        }
    }
}