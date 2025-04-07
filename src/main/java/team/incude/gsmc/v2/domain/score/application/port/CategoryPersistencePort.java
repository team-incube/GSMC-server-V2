package team.incude.gsmc.v2.domain.score.application.port;

import team.incude.gsmc.v2.domain.score.domain.Category;
import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.port.Port;

import java.util.Optional;

@Port(direction = PortDirection.OUTBOUND)
public interface CategoryPersistencePort {
    Optional<Category> findCategoryByName(String name);
}