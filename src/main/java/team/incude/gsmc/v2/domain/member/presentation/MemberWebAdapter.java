package team.incude.gsmc.v2.domain.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<Object> searchStudents() {
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
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
