package team.incude.gsmc.v2.domain.sheet.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.sheet.application.port.SheetApplicationPort;

import java.io.IOException;

@RestController
@RequestMapping("/api/v2/sheet")
@RequiredArgsConstructor
public class SheetWebAdapter {

    private final SheetApplicationPort sheetApplicationPort;

    @GetMapping("/{grade}/{classNumber}")
    public ResponseEntity<byte[]> getSheet(@PathVariable Integer grade, @PathVariable Integer classNumber) throws IOException {
        MultipartFile file = sheetApplicationPort.getSheet(grade, classNumber);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalFilename() + "\"")
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(file.getBytes());
    }

}