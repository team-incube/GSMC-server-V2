package team.incude.gsmc.v2.domain.score.application.usecase.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.member.application.port.StudentDetailPersistencePort;
import team.incude.gsmc.v2.domain.member.domain.StudentDetail;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.application.usecase.CalculateTotalScoreUseCase;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.util.SimulateScoreUtil;
import team.incude.gsmc.v2.global.util.SnakeKebabToCamelCaseConverterUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@Transactional
public class CalculateTotalScoreService implements CalculateTotalScoreUseCase {

    private final ScorePersistencePort scorePersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;
    private final StudentDetailPersistencePort studentDetailPersistencePort;

    @Override
    public void execute(String studentCode) {
        List<Score> scores = scorePersistencePort.findScoreByStudentDetailStudentCode(studentCode);
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
        StudentDetail studentDetail = studentDetailPersistencePort.findStudentDetailByStudentCode(studentCode);
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
                        ))
                        .build()
        );
    }
}