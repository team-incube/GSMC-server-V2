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