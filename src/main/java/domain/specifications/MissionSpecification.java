package domain.specifications;

import domain.models.Mission;

public interface MissionSpecification {
    boolean isSatisfiedBy(Mission mission);
}
