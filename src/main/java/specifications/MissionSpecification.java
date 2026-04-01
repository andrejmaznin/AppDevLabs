package specifications;

import models.Mission;

public interface MissionSpecification {
    boolean isSatisfiedBy(Mission mission);
}
