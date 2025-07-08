package team.incube.gsmc.v2.domain.sheet.presentation;

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
import team.incube.gsmc.v2.domain.sheet.application.port.SheetApplicationPort;

import java.io.IOException;

/**
 * 학급별 시트 파일 다운로드를 처리하는 Web 어댑터 클래스입니다.
 * <p>{@link SheetApplicationPort}를 통해 시트 파일을 조회하며, 특정 학년/반의 정보를 기준으로 시트를 반환합니다.
 * <p>제공 API:
 * <ul>
 *   <li>{@code GET /api/v2/sheet/{grade}/{classNumber}} - 해당 학년/반의 시트를 파일로 다운로드</li>
 * </ul>
 * 반환되는 파일은 HTTP 응답 본문에 {@code byte[]} 형태로 포함되며, Content-Disposition 헤더를 통해 다운로드 형식으로 전송됩니다.
 * @author snowykte0426
 */
@RestController
@RequestMapping("/api/v2/sheet")
@RequiredArgsConstructor
public class SheetWebAdapter {

    private final SheetApplicationPort sheetApplicationPort;

    /**
     * 특정 학년/반의 시트를 조회하여 파일로 반환합니다.
     * @param grade 학년 (예: 1, 2, 3)
     * @param classNumber 반 번호 (예: 1, 2)
     * @return 다운로드 가능한 바이너리 파일 응답
     * @throws IOException 파일 읽기 실패 시 발생
     */
    @GetMapping("/{grade}/{classNumber}")
    public ResponseEntity<byte[]> getSheet(@PathVariable Integer grade, @PathVariable Integer classNumber) throws IOException {
        MultipartFile file = sheetApplicationPort.getSheet(grade, classNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalFilename() + "\"")
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(file.getBytes());
    }

    /**
     * 모든 학급에 대한 시트 파일을 조회하여 파일로 반환합니다.
     * @return 다운로드 가능한 바이너리 파일 응답
     * @throws IOException 파일 읽기 실패 시 발생
     */
    @GetMapping
    public ResponseEntity<byte[]> getAllSheets() throws IOException {
        MultipartFile file = sheetApplicationPort.getAllSheets();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalFilename() + "\"")
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(file.getBytes());
    }
}