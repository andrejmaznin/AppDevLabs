package domain.specifications;

import domain.models.Mission;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecificationsTest {

    @Test
    void testOutcomeSpecification() {
        Mission m = new Mission();
        m.setOutcome("SUCCESS");

        OutcomeSpecification spec = new OutcomeSpecification("SUCCESS");
        assertTrue(spec.isSatisfiedBy(m));

        OutcomeSpecification specFail = new OutcomeSpecification("FAIL");
        assertFalse(specFail.isSatisfiedBy(m));
    }

    @Test
    void testMissionIdSpecification() {
        Mission m = new Mission();
        m.setMissionId("M-123");

        MissionIdSpecification spec = new MissionIdSpecification("M-123");
        assertTrue(spec.isSatisfiedBy(m));

        assertFalse(new MissionIdSpecification("M-456").isSatisfiedBy(m));
    }

    @Test
    void testAndSpecification() {
        Mission m = new Mission();
        m.setMissionId("M-1");
        m.setOutcome("SUCCESS");

        MissionSpecification spec = new AndSpecification(
            new MissionIdSpecification("M-1"),
            new OutcomeSpecification("SUCCESS")
        );
        assertTrue(spec.isSatisfiedBy(m));

        MissionSpecification specFail = new AndSpecification(
            new MissionIdSpecification("M-1"),
            new OutcomeSpecification("FAIL")
        );
        assertFalse(specFail.isSatisfiedBy(m));
    }

    @Test
    void testFullTextSpecification() {
        Mission m = new Mission();
        m.setLocation("Shibuya Tokyo");
        m.setComment("High cursed energy detected");

        FullTextSpecification spec = new FullTextSpecification("Shibuya Tokyo");
        assertTrue(spec.isSatisfiedBy(m));

        assertTrue(new FullTextSpecification("energy").isSatisfiedBy(m));
        assertFalse(new FullTextSpecification("Kyoto").isSatisfiedBy(m));
    }
}
