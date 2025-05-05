package team.incude.gsmc.v2.domain.member.presentation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.incude.gsmc.v2.domain.member.application.port.MemberApplicationPort;
import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;
import team.incude.gsmc.v2.domain.member.presentation.data.response.SearchStudentResponse;

import java.util.List;
import team.incude.gsmc.v2.domain.member.presentation.data.response.GetStudentResponse;
import team.incude.gsmc.v2.domain.member.presentation.data.response.SearchStudentResponse;

import java.util.List;

/**
 * 사용자 관련 HTTP 요청을 처리하는 Web 어댑터 클래스입니다.
 * <p>{@link MemberApplicationPort}를 통해 도메인 계층의 유스케이스를 실행하며,
 * 학생 목록 조회, 검색, 본인 정보 조회, 특정 학생 정보 조회 등의 기능을 제공합니다.
 * <p>제공 API:
 * <ul>
 *   <li>{@code GET /api/v2/members/students} - 전체 학생 목록 조회</li>
 *   <li>{@code GET /api/v2/members/students/search} - 학생 조건 검색</li>
 *   <li>{@code GET /api/v2/members/students/current} - 현재 로그인한 학생 정보 조회</li>
 *   <li>{@code GET /api/v2/members/students/{studentCode}} - 특정 학생 정보 조회</li>
 * </ul>
 * @author snowykte0426
 */
@RestController
@RequestMapping("/api/v2/members")
@RequiredArgsConstructor
public class MemberWebAdapter {

    private final MemberApplicationPort memberApplicationPort;

    @GetMapping("/students")
    public ResponseEntity<List<GetStudentResponse>> getAllStudents() {
        return ResponseEntity.status(HttpStatus.OK).body(memberApplicationPort.findAllStudents());
    }

    @GetMapping("/students/search")
    @Validated
    public ResponseEntity<SearchStudentResponse> searchStudents(
            @RequestParam(value = "grade", required = false) Integer grade,
            @RequestParam(value = "classNumber", required = false) Integer classNumber,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") @Min(value = 0) Integer page,
            @RequestParam(value = "size", defaultValue = "10") @Positive Integer size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(memberApplicationPort.searchStudents(name, grade, classNumber, page, size));
    }

    @GetMapping("/students/current")
    public ResponseEntity<GetStudentResponse> getCurrentStudent() {
        return ResponseEntity.status(HttpStatus.OK).body(memberApplicationPort.findCurrentStudent());
    }

    @GetMapping("/students/{studentCode}")
    public ResponseEntity<GetStudentResponse> getStudent(@PathVariable(value = "studentCode") String studentCode) {
        return ResponseEntity.status(HttpStatus.OK).body(memberApplicationPort.findMemberByStudentCode(studentCode));
    }
}