package team.incude.gsmc.v2.domain.auth.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUseCase {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberPersistencePort memberPersistencePort;
    private final AuthenticationPersistencePort authenticationPersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;

    public void execute(String name, String email, String password) {
//        Authentication authentication = authenticationPersistencePort.findAuthenticationByEmail(email);
//        if (authentication == null || Boolean.FALSE.equals(authentication.getVerified())) {
//            throw new MemberForbiddenException();
//        }
        if (memberPersistencePort.existsMemberByEmail(email)) {
            throw new MemberExistException();
        }

        StudentDetail existedStudentDetail = studentDetailPersistencePort.findStudentDetailByStudentCode(parsingEmail(email));

        Member member = Member.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(MemberRole.ROLE_STUDENT)
                .build();

        StudentDetail studentDetail = StudentDetail.builder()
                .id(existedStudentDetail.getId())
                .member(member)
                .grade(existedStudentDetail.getGrade())
                .classNumber(existedStudentDetail.getClassNumber())
                .number(existedStudentDetail.getNumber())
                .totalScore(existedStudentDetail.getTotalScore())
                .studentCode(existedStudentDetail.getStudentCode())
                .build();

        studentDetailPersistencePort.saveStudentDetail(studentDetail);
    }

    private String parsingEmail(String email) {
        Pattern pattern = Pattern.compile("s(\\d{5})@gsm\\.hs\\.kr");
        Matcher matcher = pattern.matcher(email);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new EmailFormatInvalidException();
        }
    }
}
