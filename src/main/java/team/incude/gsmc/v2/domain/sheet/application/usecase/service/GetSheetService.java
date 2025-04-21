package team.incude.gsmc.v2.domain.sheet.application.usecase.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.domain.sheet.application.usecase.GetSheetUseCase;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 학급별 XLSX 생성 서비스 – 전공/인문·인성/외국어 **3개 시트**를 생성한다.
 * <p>
 * 1. 카테고리를 Area(MAJOR/HUMANITIES/FOREIGN_LANG) 별로 그룹화<br>
 * 2. 각 시트마다 <code>[번호 | 이름 | 카테고리별 점수 … | 모든 영역 합계 | 반 순위]</code><br>
 * 3. 두 줄짜리 헤더(1단계: 상위 그룹, 2단계: 개별 카테고리) 작성<br>
 * 4. 학생별 점수를 매핑하여 행 추가<br>
 * 5. Auto‑size & 스타일 적용 후 in‑memory MultipartFile 반환
 */
@Service
@RequiredArgsConstructor
public class GetSheetService implements GetSheetUseCase {

    private final ScorePersistencePort scorePort;
    private final CategoryPersistencePort categoryPort;
    private final StudentDetailPersistencePort studentPort;

    @Override
    @Transactional(readOnly = true)
    public MultipartFile execute(Integer grade, Integer classNumber) {
        // --- 1. 메타데이터 준비 ---------------------------------------------------
        List<Category> allCategories = categoryPort.findAllCategory();
        Map<Area, List<Category>> categoriesByArea = allCategories.stream()
                .collect(Collectors.groupingBy(this::resolveArea, LinkedHashMap::new, Collectors.toList()));

        List<StudentDetail> students = new ArrayList<>(
                studentPort.findStudentDetailsByGradeAndClassNumber(grade, classNumber)
        );
        // 학생번호 정렬
        students.sort(Comparator.comparingInt(StudentDetail::getNumber));

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            CellStyle headerStyle = createHeaderStyle(wb);
            CellStyle sectionStyle = createSectionHeaderStyle(wb);

            // --- 2. Area 별 시트 ---------------------------------------------------
            for (Area area : Area.values()) {
                List<Category> categories = categoriesByArea.getOrDefault(area, List.of());
                createAreaSheet(wb, area, categories, students, headerStyle, sectionStyle);
            }

            // --- 3. Workbook → MultipartFile 변환 ---------------------------------
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            wb.write(baos);
            String filename = grade + "-" + classNumber + "-scores.xlsx";
            return new InMemoryMultipartFile(
                    "file",
                    filename,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    baos.toByteArray()
            );
        } catch (IOException e) {
            throw new IllegalStateException("엑셀 생성 중 오류", e);
        }
    }

    // -------------------------------------------------------------------------
    // Sheet 생성 로직
    // -------------------------------------------------------------------------
    private void createAreaSheet(Workbook wb,
                                 Area area,
                                 List<Category> categories,
                                 List<StudentDetail> students,
                                 CellStyle headerStyle,
                                 CellStyle sectionStyle) {
        Sheet sheet = wb.createSheet(area.getDisplayName());

        /* --------------------------- 헤더 --------------------------- */
        // 2단계 헤더를 위해 row 0, row 1 생성
        Row groupRow = sheet.createRow(0);
        Row headerRow = sheet.createRow(1);

        int colIdx = 0;
        // 번호, 이름 고정 컬럼
        createHeaderCell(groupRow, colIdx, "", headerStyle);
        createHeaderCell(headerRow, colIdx++, "번호", headerStyle);
        createHeaderCell(groupRow, colIdx, "", headerStyle);
        createHeaderCell(headerRow, colIdx++, "이름", headerStyle);

        // 카테고리 → Grouping(상위) → 개별 헤더(하위)
        Map<String, List<Category>> grouped = categories.stream()
                .collect(Collectors.groupingBy(this::extractGroupName, LinkedHashMap::new, Collectors.toList()));

        for (Map.Entry<String, List<Category>> entry : grouped.entrySet()) {
            String groupName = entry.getKey();
            List<Category> cates = entry.getValue();
            int startCol = colIdx;
            for (Category c : cates) {
                createHeaderCell(headerRow, colIdx++, c.getName(), headerStyle);
            }
            // 병합 (상위 그룹 헤더) – 1열만 있을 때는 병합 안 함
            if (cates.size() > 1) {
                sheet.addMergedRegion(new CellRangeAddress(0, 0, startCol, startCol + cates.size() - 1));
            }
            createHeaderCell(groupRow, startCol, groupName, sectionStyle);
        }

        // 총합, 순위
        createHeaderCell(groupRow, colIdx, "", headerStyle);
        createHeaderCell(headerRow, colIdx++, "모든 영역 합계", headerStyle);
        createHeaderCell(groupRow, colIdx, "", headerStyle);
        createHeaderCell(headerRow, colIdx, "반 순위", headerStyle);

        /* --------------------------- 데이터 --------------------------- */
        int rowIdx = 2;
        for (StudentDetail student : students) {
            Row row = sheet.createRow(rowIdx++);
            int dataCol = 0;
            row.createCell(dataCol++).setCellValue(student.getNumber());
            row.createCell(dataCol++).setCellValue(student.getMember().getName());

            List<Score> scoreList = scorePort.findScoreByStudentDetailStudentCode(student.getStudentCode());
            Map<String, Score> scoreMap = scoreList.stream()
                    .collect(Collectors.toMap(s -> s.getCategory().getName(), Function.identity()));

            int total = student.getTotalScore();
            for (Category c : categories) {
                int point = Optional.ofNullable(scoreMap.get(c.getName()))
                        .map(Score::getValue)
                        .orElse(0);
                row.createCell(dataCol++).setCellValue(point);
            }
            row.createCell(dataCol++).setCellValue(total);
            // 순위는 컨트롤러/DB 에서 계산하거나, 빈칸으로 두고 사용자가 입력하도록 두는 등 정책 결정 필요
            row.createCell(dataCol).setCellValue(1); // placeholder
        }

        // Auto‑size
        for (int i = 0; i < colIdx + 1; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private String extractGroupName(Category c) {
        // 카테고리 코드 규칙: MAJOR-AWARD_CAREER-IN_SCHOOL-XXX
        // 상위 그룹은 두 번째 토큰까지(AWARD_CAREER, CERTIFICATE 등)로 정의
        String[] parts = c.getName().split("-");
        return parts.length > 2 ? parts[1] : "기타";
    }

    private Area resolveArea(Category c) {
        if (c.getName().startsWith("MAJOR")) return Area.MAJOR;
        if (c.getName().startsWith("HUMANITIES")) return Area.HUMANITIES;
        if (c.getName().startsWith("FOREIGN_LANG")) return Area.FOREIGN_LANG;
        throw new IllegalArgumentException("Unknown area prefix: " + c.getName());
    }

    private CellStyle createHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createSectionHeaderStyle(Workbook wb) {
        CellStyle style = createHeaderStyle(wb);
        style.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
        return style;
    }

    private void createHeaderCell(Row row, int colIdx, String value, CellStyle style) {
        Cell cell = row.createCell(colIdx);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    // -------------------------------------------------------------------------
    // 커스텀 in‑memory MultipartFile 구현체
    // -------------------------------------------------------------------------
    private static final class InMemoryMultipartFile implements MultipartFile {
        private final String name;
        private final String filename;
        private final String contentType;
        private final byte[] content;

        private InMemoryMultipartFile(String name, String filename, String contentType, byte[] content) {
            this.name = name;
            this.filename = filename;
            this.contentType = contentType;
            this.content = content;
        }


        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return filename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() {
            return content.clone();
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            try (OutputStream os = new FileOutputStream(dest)) {
                os.write(content);
            }
        }

        @Override
        public void transferTo(Path dest) throws IOException {
            Files.write(dest, content);
        }
    }

    // -------------------------------------------------------------------------
    // Area enum
    // -------------------------------------------------------------------------
    @Getter
    private enum Area {
        MAJOR("전공 영역"),
        HUMANITIES("인문·인성 영역"),
        FOREIGN_LANG("외국어 영역");

        private final String displayName;

        Area(String displayName) {
            this.displayName = displayName;
        }
    }
}