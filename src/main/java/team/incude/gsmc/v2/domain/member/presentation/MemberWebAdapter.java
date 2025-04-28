package team.incude.gsmc.v2.domain.member.presentation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.incude.gsmc.v2.domain.member.application.port.MemberApplicationPort;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v2/members")
@RequiredArgsConstructor
public class MemberWebAdapter {

    private final MemberApplicationPort memberApplicationPort;

    @GetMapping("/students")
    public ResponseEntity<Object> getAllStudents() {
        return ResponseEntity.status(HttpStatus.OK).body(memberApplicationPort.findAllStudents());
    }

    @GetMapping("/students/search")
    @Validated
    public ResponseEntity<Object> searchStudents(
            @RequestParam(value = "grade", required = false) Integer grade,
            @RequestParam(value = "classNumber", required = false) Integer classNumber,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") @Min(value = 0) Integer page,
            @RequestParam(value = "size", defaultValue = "10") @Positive Integer size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(memberApplicationPort.searchStudents(name, grade, classNumber, page, size));
    }

    @GetMapping("/students/current")
    public ResponseEntity<Object> getCurrentStudent() {
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
    }

    @GetMapping("/students/{studentCode}")
    public ResponseEntity<Object> getStudent(@PathVariable(value = "studentCode") String studentCode) {
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
    }
}
