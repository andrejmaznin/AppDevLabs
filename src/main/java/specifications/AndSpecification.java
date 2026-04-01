package specifications;

import models.Mission;

public class AndSpecification implements MissionSpecification {
    private final MissionSpecification spec1;
    private final MissionSpecification spec2;

    public AndSpecification(MissionSpecification spec1, MissionSpecification spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    @Override
    public boolean isSatisfiedBy(Mission mission) {
        return spec1.isSatisfiedBy(mission) && spec2.isSatisfiedBy(mission);
    }
}