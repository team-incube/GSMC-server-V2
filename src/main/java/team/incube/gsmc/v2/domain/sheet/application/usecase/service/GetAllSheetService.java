package team.incube.gsmc.v2.domain.sheet.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.function.Tuple4;
import team.incube.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incube.gsmc.v2.domain.member.domain.StudentDetail;
import team.incube.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incube.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incube.gsmc.v2.domain.score.domain.Category;
import team.incube.gsmc.v2.domain.score.domain.Score;
import team.incube.gsmc.v2.domain.score.exception.InvalidCategoryException;
import team.incube.gsmc.v2.domain.sheet.application.usecase.GetAllSheetUseCase;
import team.incube.gsmc.v2.domain.sheet.domain.InMemoryMultipartFile;
import team.incube.gsmc.v2.domain.sheet.domain.constant.CategoryArea;
import team.incube.gsmc.v2.domain.sheet.exception.GenerationSheetFailedException;
import team.incube.gsmc.v2.global.util.SimulateScoreUtil;
import team.incube.gsmc.v2.global.util.SnakeKebabToCamelCaseConverterUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 모든 학급의 점수표 시트 파일을 생성하는 유스케이스의 구현체입니다.
 * <p>{@link GetAllSheetUseCase}를 구현하며, 모든 학년/반 조합에 대해 영역별로 시트를 생성합니다.
 * 각 시트는 "학년-반(영역명)" 형식으로 명명되며 (예: "2-1(전공 영역)", "3-2(외국어 영역)"),
 * Apache POI를 사용하여 다중 시트, 계층적 헤더, 영역별 합계, 총합, 순위 등을 포함한 포맷을 구성합니다.
 * <p>생성된 시트는 {@link InMemoryMultipartFile}로 반환됩니다.
 * 예외 발생 시 {@link GenerationSheetFailedException}을 통해 오류를 처리합니다.
 * @author snowykte0426
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAllSheetService implements GetAllSheetUseCase {

    private final ScorePersistencePort scorePersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;

    /**
     * 모든 학급에 대해 영역별 시트를 포함한 Excel 파일을 생성합니다.
     * 각 학급의 각 영역별로 별도 시트가 생성되며, 시트명은 "학년-반(영역명)" 형식입니다.
     * @return 생성된 Excel 시트를 포함한 MultipartFile 객체
     * @throws GenerationSheetFailedException 파일 생성 중 오류 발생 시
     */
    @Override
    public MultipartFile execute() {
        List<Category> allCats = categoryPersistencePort.findAllCategory();
        Set<GradeClassInfo> gradeClassCombinations = extractGradeClassCombinations();
        Map<String, Float> weights = allCats.stream()
                .collect(Collectors.toMap(
                        c -> SnakeKebabToCamelCaseConverterUtil.toCamelCase(c.getName()),
                        Category::getWeight
                ));

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            CellStyle headerStyle = createHeaderStyle(wb);
            CellStyle sectionStyle = createSectionStyle(wb);

            for (GradeClassInfo gradeClass : gradeClassCombinations) {
                generateSheetsForClass(wb, gradeClass, allCats, weights, headerStyle, sectionStyle);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            wb.write(baos);

            String filename = URLEncoder.encode("전체-학급-점수표.xlsx", StandardCharsets.UTF_8);
            return new InMemoryMultipartFile(
                    "file",
                    filename,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    baos.toByteArray()
            );
        } catch (IOException e) {
            throw new GenerationSheetFailedException();
        }
    }

    /**
     * 모든 학생 정보를 조회하여 존재하는 학년/반 조합을 추출합니다.
     * @return 학년/반 조합 집합
     */
    private Set<GradeClassInfo> extractGradeClassCombinations() {
        Set<GradeClassInfo> combinations = new HashSet<>();
        for (int grade = 1; grade <= 3; grade++) {
            for (int classNumber = 1; classNumber <= 4; classNumber++) {
                List<StudentDetail> students = studentDetailPersistencePort
                        .findStudentDetailByGradeAndClassNumberAndMemberNotNull(grade, classNumber);
                if (!students.isEmpty()) {
                    combinations.add(new GradeClassInfo(grade, classNumber));
                }
            }
        }
        return combinations;
    }

    /**
     * 특정 학급에 대해 영역별 시트를 생성합니다.
     * @param wb Excel 워크북
     * @param gradeClass 학년/반 정보
     * @param allCats 전체 카테고리 목록
     * @param weights 카테고리 가중치 맵
     * @param headerStyle 헤더 셀 스타일
     * @param sectionStyle 상위 섹션 셀 스타일
     */
    private void generateSheetsForClass(XSSFWorkbook wb, GradeClassInfo gradeClass, List<Category> allCats, Map<String, Float> weights, CellStyle headerStyle, CellStyle sectionStyle) {
        
        List<StudentDetail> students = new ArrayList<>(
                studentDetailPersistencePort.findStudentDetailByGradeAndClassNumberAndMemberNotNull(
                        gradeClass.grade, gradeClass.classNumber)
        );
        
        if (students.isEmpty()) {
            return;
        }
        
        students.sort(Comparator.comparingInt(StudentDetail::getNumber));

        Map<Long, String> memberIdToStudentCode = students.stream()
                .collect(Collectors.toMap(
                        s -> s.getMember().getId(),
                        StudentDetail::getStudentCode
                ));

        List<Score> scores = scorePersistencePort.findScoreByStudentDetailStudentCodes(
                students.stream().map(StudentDetail::getStudentCode).toList()
        );

        Map<String, List<Score>> scoreMap = scores.stream()
                .collect(Collectors.groupingBy(score -> {
                    Long memberId = score.getMember().getId();
                    return memberIdToStudentCode.get(memberId);
                }));

        Map<String, Integer> rankMap = calculateRanks(students);

        for (CategoryArea area : CategoryArea.values()) {
            List<Category> cats = allCats.stream()
                    .filter(c -> resolveArea(c) == area)
                    .collect(Collectors.toList());

            String sheetTitle = String.format("%d-%d(%s)", 
                    gradeClass.grade, gradeClass.classNumber, area.getDisplayName());

            buildSheet(
                    wb,
                    sheetTitle,
                    allCats,
                    cats,
                    students,
                    scoreMap,
                    weights,
                    rankMap,
                    headerStyle,
                    sectionStyle
            );
        }
    }

    /**
     * 학생들의 총점을 기준으로 반 내 순위를 계산합니다.
     * @param students 학생 리스트
     * @return 학생 코드별 순위 맵
     */
    private Map<String, Integer> calculateRanks(List<StudentDetail> students) {
        Map<String, Integer> rankMap = new HashMap<>();
        List<StudentDetail> sorted = new ArrayList<>(students);
        sorted.sort(Comparator.comparingInt(StudentDetail::getTotalScore).reversed());

        int prevScore = Integer.MIN_VALUE, rank = 0;
        for (StudentDetail sd : sorted) {
            int sc = sd.getTotalScore();
            if (sc != prevScore) {
                rank++;
                prevScore = sc;
            }
            rankMap.put(sd.getStudentCode(), rank);
        }
        return rankMap;
    }

    /**
     * 주어진 정보를 바탕으로 워크북 내의 하나의 시트를 구성합니다.
     * @param wb Excel 워크북
     * @param title 시트 이름
     * @param allCats 전체 카테고리 목록
     * @param cats 해당 영역의 카테고리 목록
     * @param students 학생 목록
     * @param scoreMap 학생별 점수 맵
     * @param weights 카테고리 가중치 맵
     * @param rankMap 학생별 반 내 순위 맵
     * @param headerStyle 헤더 셀 스타일
     * @param sectionStyle 상위 섹션 셀 스타일
     */
    private void buildSheet(Workbook wb, String title, List<Category> allCats, List<Category> cats, List<StudentDetail> students,
                            Map<String, List<Score>> scoreMap, Map<String, Float> weights, Map<String, Integer> rankMap,
                            CellStyle headerStyle, CellStyle sectionStyle) {

        Sheet sheet = wb.createSheet(title);
        sheet.createFreezePane(2, 1);

        List<String[]> splitLabels = cats.stream()
                .map(c -> c.getKoreanName().split("-", -1))
                .toList();
        int depth = splitLabels.stream().mapToInt(arr -> arr.length).max().orElse(1);
        Row[] hdr = new Row[depth];
        for (int i = 0; i < depth; i++) hdr[i] = sheet.createRow(i);

        sheet.addMergedRegion(new CellRangeAddress(0, depth - 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, depth - 1, 1, 1));
        Cell numCell = hdr[depth - 1].createCell(0);
        numCell.setCellValue("번호");
        numCell.setCellStyle(headerStyle);
        Cell nameCell = hdr[depth - 1].createCell(1);
        nameCell.setCellValue("이름");
        nameCell.setCellStyle(headerStyle);

        int col = 2;
        for (int i = 0; i < cats.size(); i++) {
            String[] parts = splitLabels.get(i);
            int cidx = col++;

            for (int lvl = 0; lvl < parts.length; lvl++) {
                Cell c = hdr[lvl].createCell(cidx);
                c.setCellValue(parts[lvl]);
                c.setCellStyle(lvl == 0 ? sectionStyle : headerStyle);
            }

            for (int lvl = parts.length; lvl < depth; lvl++) {
                Cell blank = hdr[lvl].createCell(cidx);
                blank.setCellStyle(headerStyle);
            }
        }

        for (int lvl = 0; lvl < depth; lvl++) {
            int start = 2;
            String prev = Optional.ofNullable(hdr[lvl].getCell(2)).map(Cell::getStringCellValue).orElse("");
            for (int x = 3; x < col; x++) {
                String cur = Optional.ofNullable(hdr[lvl].getCell(x)).map(Cell::getStringCellValue).orElse("");
                if (!Objects.equals(prev, cur)) {
                    if (!prev.isEmpty() && x - 1 > start) {
                        sheet.addMergedRegion(new CellRangeAddress(lvl, lvl, start, x - 1));
                    }
                    prev = cur;
                    start = x;
                }
            }
            if (!prev.isEmpty() && col - 1 > start) {
                sheet.addMergedRegion(new CellRangeAddress(lvl, lvl, start, col - 1));
            }
        }

        int areaCol = col;
        int totalCol = col + 1;
        int rankCol = col + 2;

        Cell areaH = hdr[0].createCell(areaCol);
        areaH.setCellValue("영역 합계");
        areaH.setCellStyle(headerStyle);
        Cell totH = hdr[0].createCell(totalCol);
        totH.setCellValue("모든 영역 합계");
        totH.setCellStyle(headerStyle);
        Cell rkH = hdr[0].createCell(rankCol);
        rkH.setCellValue("반 순위");
        rkH.setCellStyle(headerStyle);

        if (depth > 1) {
            sheet.addMergedRegion(new CellRangeAddress(0, depth - 1, areaCol, areaCol));
            sheet.addMergedRegion(new CellRangeAddress(0, depth - 1, totalCol, totalCol));
            sheet.addMergedRegion(new CellRangeAddress(0, depth - 1, rankCol, rankCol));
        }

        int rowIdx = depth;
        for (StudentDetail s : students) {
            Row r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue(s.getNumber());
            r.createCell(1).setCellValue(s.getMember().getName());

            Map<String, Integer> raw = scoreMap.getOrDefault(s.getStudentCode(), Collections.emptyList())
                    .stream().collect(Collectors.toMap(
                            sc -> SnakeKebabToCamelCaseConverterUtil.toCamelCase(sc.getCategory().getName()),
                            Score::getValue, (a, b) -> a
                    ));

            Tuple4<Integer, Integer, Integer, Integer> t = SimulateScoreUtil.simulateScore(
                    raw.getOrDefault("majorAwardCareerOutSchoolOfficial", 0),
                    raw.getOrDefault("majorAwardCareerOutSchoolUnofficial", 0),
                    raw.getOrDefault("majorAwardCareerOutSchoolHackathon", 0),
                    raw.getOrDefault("majorAwardCareerInSchoolGsmfest", 0),
                    raw.getOrDefault("majorAwardCareerInSchoolSchoolHackathon", 0),
                    raw.getOrDefault("majorAwardCareerInSchoolPresentation", 0),
                    raw.getOrDefault("majorCertificateNum", 0),
                    raw.getOrDefault("majorTopcitScore", 0),
                    raw.getOrDefault("majorClubAttendanceSemester1", 0),
                    raw.getOrDefault("majorClubAttendanceSemester2", 0),
                    raw.getOrDefault("majorOutSchoolAttendanceOfficialContest", 0),
                    raw.getOrDefault("majorOutSchoolAttendanceUnofficialContest", 0),
                    raw.getOrDefault("majorOutSchoolAttendanceHackathon", 0),
                    raw.getOrDefault("majorOutSchoolAttendanceSeminar", 0),
                    raw.getOrDefault("majorInSchoolAttendanceGsmfest", 0),
                    raw.getOrDefault("majorInSchoolAttendanceHackathon", 0),
                    raw.getOrDefault("majorInSchoolAttendanceClubPresentation", 0),
                    raw.getOrDefault("majorInSchoolAttendanceSeminar", 0),
                    raw.getOrDefault("majorInSchoolAttendanceAfterSchool", 0),
                    raw.getOrDefault("humanitiesAwardCareerHumanityInSchool", 0),
                    raw.getOrDefault("humanitiesAwardCareerHumanityOutSchool", 0),
                    raw.getOrDefault("humanitiesReadingReadAThonTurtle", 0) == 1,
                    raw.getOrDefault("humanitiesReadingReadAThonCrocodile", 0) == 1,
                    raw.getOrDefault("humanitiesReadingReadAThonRabbitOver", 0) == 1,
                    raw.getOrDefault("humanitiesReading", 0),
                    raw.getOrDefault("humanitiesServiceActivity", 0),
                    raw.getOrDefault("humanitiesServiceClubSemester1", 0),
                    raw.getOrDefault("humanitiesServiceClubSemester2", 0),
                    raw.getOrDefault("humanitiesCertificateChineseCharacter", 0) == 1,
                    raw.getOrDefault("humanitiesCertificateKoreanHistory", 0) == 1,
                    raw.getOrDefault("humanitiesActivitiesNewrrowS", 0),
                    raw.getOrDefault("humanitiesActivitiesSelfDirectedActivities", 0),
                    raw.getOrDefault("foreignLangAttendanceToeicAcademyStatus", 0) == 1,
                    raw.getOrDefault("foreignLangToeicScore", 0),
                    raw.getOrDefault("foreignLangToeflScore", 0),
                    raw.getOrDefault("foreignLangTepsScore", 0),
                    raw.getOrDefault("foreignLangToeicSpeakingLevel", 0),
                    raw.getOrDefault("foreignLangOpicGrade", 0),
                    raw.getOrDefault("foreignLangJptScore", 0),
                    raw.getOrDefault("foreignLangCptScore", 0),
                    raw.getOrDefault("foreignLangHskGrade", 0),
                    weights
            );

            int majorScore = t.getT1();
            int humanityScore = t.getT2();
            int foreignScore = t.getT3();
            int totalScore = t.getT4();

            int dc = 2;
            for (Category c : cats) {
                String key = SnakeKebabToCamelCaseConverterUtil.toCamelCase(c.getName());
                r.createCell(dc++).setCellValue(raw.getOrDefault(key, 0));
            }

            // 현재 시트의 영역에 따라 해당 영역 점수 표시
            String areaDisplayName = extractAreaFromSheetTitle(title);
            r.createCell(areaCol).setCellValue(
                    areaDisplayName.equals(CategoryArea.MAJOR.getDisplayName()) ? majorScore :
                            areaDisplayName.equals(CategoryArea.HUMANITIES.getDisplayName()) ? humanityScore : foreignScore
            );
            r.createCell(totalCol).setCellValue(totalScore);
            r.createCell(rankCol).setCellValue(rankMap.get(s.getStudentCode()));
        }

        for (int i = 0; i <= rankCol; i++) {
            sheet.setColumnWidth(i, 10 * 256);
        }
    }

    /**
     * 시트 제목에서 영역명을 추출합니다.
     * @param sheetTitle 시트 제목 (예: "2-1(전공 영역)")
     * @return 영역명 (예: "전공 영역")
     */
    private String extractAreaFromSheetTitle(String sheetTitle) {
        int startIndex = sheetTitle.indexOf('(');
        int endIndex = sheetTitle.indexOf(')');
        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return sheetTitle.substring(startIndex + 1, endIndex);
        }
        return "";
    }

    /**
     * 기본 헤더 셀 스타일을 생성합니다.
     * @param wb Excel 워크북
     * @return 생성된 셀 스타일
     */
    private CellStyle createHeaderStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBold(true);
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        s.setWrapText(true);
        s.setBorderBottom(BorderStyle.THIN);
        s.setBorderTop(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);
        s.setBorderRight(BorderStyle.THIN);
        s.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return s;
    }

    /**
     * 섹션 구분용 셀 스타일을 생성합니다.
     * @param wb Excel 워크북
     * @return 생성된 셀 스타일
     */
    private CellStyle createSectionStyle(Workbook wb) {
        CellStyle s = createHeaderStyle(wb);
        s.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
        return s;
    }

    /**
     * 카테고리 이름 접두어를 기반으로 해당 카테고리가 속한 영역을 판별합니다.
     * @param c 카테고리
     * @return 해당 카테고리의 영역 (전공, 인문, 외국어)
     * @throws InvalidCategoryException 알 수 없는 영역일 경우
     */
    private CategoryArea resolveArea(Category c) {
        String name = c.getName().toLowerCase();
        if (name.startsWith("major")) return CategoryArea.MAJOR;
        if (name.startsWith("humanities")) return CategoryArea.HUMANITIES;
        if (name.startsWith("foreign_lang")) return CategoryArea.FOREIGN_LANG;
        throw new InvalidCategoryException();
    }

    /**
     * 학년과 반 정보를 담는 내부 클래스입니다.
     */
    private static class GradeClassInfo {
        final int grade;
        final int classNumber;

        GradeClassInfo(int grade, int classNumber) {
            this.grade = grade;
            this.classNumber = classNumber;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GradeClassInfo that = (GradeClassInfo) o;
            return grade == that.grade && classNumber == that.classNumber;
        }

        @Override
        public int hashCode() {
            return Objects.hash(grade, classNumber);
        }

        @Override
        public String toString() {
            return grade + "-" + classNumber;
        }
    }
}
