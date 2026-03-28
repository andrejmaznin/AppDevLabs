package specifications;

import models.Mission;

public class MissionIdSpecification implements MissionSpecification {
    private final String targetId;

    public MissionIdSpecification(String targetId) {
        this.targetId = targetId;
    }

    @Override
    public boolean isSatisfiedBy(Mission mission) {
        if (mission == null || targetId == null) return false;
        return targetId.equals(mission.getMissionId());
    }
}