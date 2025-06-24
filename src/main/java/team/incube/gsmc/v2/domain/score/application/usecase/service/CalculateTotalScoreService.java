package team.incube.gsmc.v2.domain.score.application.usecase.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incube.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incube.gsmc.v2.domain.member.domain.StudentDetail;
import team.incube.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incube.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incube.gsmc.v2.domain.score.application.usecase.CalculateTotalScoreUseCase;
import team.incube.gsmc.v2.domain.score.domain.Category;
import team.incube.gsmc.v2.domain.score.domain.Score;
import team.incube.gsmc.v2.global.util.SimulateScoreUtil;
import team.incube.gsmc.v2.global.util.SnakeKebabToCamelCaseConverterUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * 총합 점수 계산 유스케이스의 구현체입니다.
 * <p>{@link CalculateTotalScoreUseCase}를 구현하며, 특정 학생의 모든 점수를 기반으로 총합 점수를 계산하고 저장합니다.
 * <p>처리 절차:
 * <ul>
 *   <li>카테고리 목록 조회 및 정렬</li>
 *   <li>학생의 점수를 카테고리별로 매핑</li>
 *   <li>정렬된 순서에 맞춰 점수 목록 생성</li>
 *   <li>{@link SimulateScoreUtil}을 통해 총합 점수 계산</li>
 *   <li>계산된 총합 점수를 {@link StudentDetail}에 저장</li>
 * </ul>
 * @author snowykte0426
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CalculateTotalScoreService implements CalculateTotalScoreUseCase {

    private final ScorePersistencePort scorePersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;

    /**
     * 특정 학생의 총합 점수를 계산하여 {@link StudentDetail}에 저장합니다.
     * @param email 총합 점수를 계산할 학생 이메일
     */
    @Override
    public void execute(String email) {
        List<Score> scores = scorePersistencePort.findScoreByMemberEmail(email);
        List<Category> categories = categoryPersistencePort.findAllCategory().stream()
                .map(
                        categoryEntity -> Category.builder()
                                .id(categoryEntity.getId())
                                .name(SnakeKebabToCamelCaseConverterUtil.toCamelCase(categoryEntity.getName()))
                                .weight(categoryEntity.getWeight())
                                .build()
                )
                .sorted(Comparator.comparing(Category::getId))
                .toList();
        Map<Long, Integer> scoreByCategory = scores.stream()
                .collect(Collectors.toMap(
                        s -> s.getCategory().getId(),
                        Score::getValue,
                        (ex, dup) -> ex
                ));
        List<Integer> scoreValues = categories.stream()
                .map(cat -> scoreByCategory.getOrDefault(cat.getId(), 0))
                .toList();
        StudentDetail studentDetail = studentDetailPersistencePort.findStudentDetailByEmail(email);
        studentDetailPersistencePort.saveStudentDetail(
                StudentDetail.builder()
                        .id(studentDetail.getId())
                        .studentCode(studentDetail.getStudentCode())
                        .member(studentDetail.getMember())
                        .classNumber(studentDetail.getClassNumber())
                        .grade(studentDetail.getGrade())
                        .number(studentDetail.getNumber())
                        .totalScore(SimulateScoreUtil.simulateScore(
                                scoreValues.get(0),
                                scoreValues.get(1),
                                scoreValues.get(2),
                                scoreValues.get(3),
                                scoreValues.get(4),
                                scoreValues.get(5),
                                scoreValues.get(6),
                                scoreValues.get(7),
                                scoreValues.get(8),
                                scoreValues.get(9),
                                scoreValues.get(10),
                                scoreValues.get(11),
                                scoreValues.get(12),
                                scoreValues.get(13),
                                scoreValues.get(14),
                                scoreValues.get(15),
                                scoreValues.get(16),
                                scoreValues.get(17),
                                scoreValues.get(18),
                                scoreValues.get(19),
                                scoreValues.get(20),
                                scoreValues.get(21) == 1,
                                scoreValues.get(22) == 1,
                                scoreValues.get(23) == 1,
                                scoreValues.get(24),
                                scoreValues.get(25),
                                scoreValues.get(26),
                                scoreValues.get(27),
                                scoreValues.get(28) == 1,
                                scoreValues.get(29) == 1,
                                scoreValues.get(30),
                                scoreValues.get(31),
                                scoreValues.get(32) == 1,
                                scoreValues.get(33),
                                scoreValues.get(34),
                                scoreValues.get(35),
                                scoreValues.get(36),
                                scoreValues.get(37),
                                scoreValues.get(38),
                                scoreValues.get(39),
                                scoreValues.get(40),
                                categories.stream()
                                        .collect(toMap(
                                                Category::getName,
                                                Category::getWeight
                                        ))
                        ).getT4())
                        .build()
        );
    }
}