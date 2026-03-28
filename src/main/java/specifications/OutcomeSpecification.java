package specifications;

import models.Mission;

public class OutcomeSpecification implements MissionSpecification {
    private final String expectedOutcome;

    public OutcomeSpecification(String expectedOutcome) {
        this.expectedOutcome = expectedOutcome;
    }

    @Override
    public boolean isSatisfiedBy(Mission mission) {
        if (mission == null || expectedOutcome == null) return false;
        return expectedOutcome.equalsIgnoreCase(mission.getOutcome());
    }
}