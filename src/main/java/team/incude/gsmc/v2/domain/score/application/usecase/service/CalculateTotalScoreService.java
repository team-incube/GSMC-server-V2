package team.incude.gsmc.v2.domain.score.application.usecase.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incude.gsmc.v2.domain.score.application.port.CategoryPersistencePort;
import team.incude.gsmc.v2.domain.score.application.port.ScorePersistencePort;
import team.incude.gsmc.v2.domain.score.application.usecase.CalculateTotalScoreUseCase;
import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.domain.score.domain.Score;
import team.incude.gsmc.v2.global.util.SimulateScoreUtil;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CalculateTotalScoreService implements CalculateTotalScoreUseCase {

    private final ScorePersistencePort scorePersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;

    @Override
    public void execute(String studentCode) {
        List<Score> scores = scorePersistencePort.findScoreByStudentDetailStudentCode(studentCode);
        List<Category> categories = categoryPersistencePort.findAllCategory().stream().sorted().toList();
        Map<String, Float> categoryWeights = categories.stream().map(
                category -> Map.entry(category.getName(), category.getWeight())
        ).reduce(
                Map.of(),
                (acc, entry) -> {
                    acc.put(entry.getKey(), entry.getValue());
                    return acc;
                },
                (acc1, acc2) -> {
                    acc1.putAll(acc2);
                    return acc1;
                }
        );
        List<String> categoryNames = scores.stream().map(
                score -> score.getCategory().getName()
        ).toList();
        SimulateScoreUtil.simulateScore(
                Integer.parseInt(categoryNames.getFirst()),
                Integer.parseInt(categoryNames.get(1)),
                Integer.parseInt(categoryNames.get(2)),
                Integer.parseInt(categoryNames.get(3)),
                Integer.parseInt(categoryNames.get(4)),
                Integer.parseInt(categoryNames.get(5)),
                Integer.parseInt(categoryNames.get(6)),
                Integer.parseInt(categoryNames.get(7)),
                Integer.parseInt(categoryNames.get(8)),
                Integer.parseInt(categoryNames.get(9)),
                Integer.parseInt(categoryNames.get(10)),
                Integer.parseInt(categoryNames.get(11)),
                Integer.parseInt(categoryNames.get(12)),
                Integer.parseInt(categoryNames.get(13)),
                Integer.parseInt(categoryNames.get(14)),
                Integer.parseInt(categoryNames.get(15)),
                Integer.parseInt(categoryNames.get(16)),
                Integer.parseInt(categoryNames.get(17)),
                Integer.parseInt(categoryNames.get(18)),
                Integer.parseInt(categoryNames.get(19)),
                Integer.parseInt(categoryNames.get(20)),
                Boolean.parseBoolean(categoryNames.get(21)),
                Boolean.parseBoolean(categoryNames.get(22)),
                Boolean.parseBoolean(categoryNames.get(23)),
                Integer.parseInt(categoryNames.get(24)),
                Integer.parseInt(categoryNames.get(25)),
                Integer.parseInt(categoryNames.get(26)),
                Integer.parseInt(categoryNames.get(27)),
                Boolean.parseBoolean(categoryNames.get(28)),
                Boolean.parseBoolean(categoryNames.get(29)),
                Integer.parseInt(categoryNames.get(30)),
                Integer.parseInt(categoryNames.get(31)),
                Boolean.parseBoolean(categoryNames.get(32)),
                Integer.parseInt(categoryNames.get(33)),
                Integer.parseInt(categoryNames.get(34)),
                Integer.parseInt(categoryNames.get(35)),
                Integer.parseInt(categoryNames.get(36)),
                Integer.parseInt(categoryNames.get(37)),
                Integer.parseInt(categoryNames.get(38)),
                Integer.parseInt(categoryNames.get(39)),
                Integer.parseInt(categoryNames.get(40)),
                categoryWeights
        );
    }
}