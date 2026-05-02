package domain.validation;

import domain.models.Curse;
import domain.models.Mission;
import domain.models.Sorcerer;
import domain.models.Technique;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    private Mission mission;

    @BeforeEach
    void setUp() {
        mission = new Mission();
        mission.setMissionId("M-1");
        mission.setDate("2024-05-02");
        mission.setLocation("Tokyo");
        mission.setOutcome("SUCCESS");
        
        Curse curse = new Curse();
        curse.setName("Sukuna");
        curse.setThreatLevel("SPECIAL");
        mission.setCurse(curse);

        Sorcerer s = new Sorcerer();
        s.setName("Gojo");
        s.setRank("SPECIAL");
        mission.setSorcerers(new ArrayList<>(Collections.singletonList(s)));

        Technique t = new Technique();
        t.setName("Blue");
        t.setType("INNATE");
        t.setOwner("Gojo");
        mission.setTechniques(new ArrayList<>(Collections.singletonList(t)));
    }

    @Test
    void testDefaultChain_Success() {
        assertDoesNotThrow(() -> ValChainFactory.createDefaultChain().validate(mission));
    }

    @Test
    void testMissionBasicValidator_Fail() {
        mission.setMissionId(null);
        assertThrows(ValidationException.class, () -> new MissionBasicValidator().validate(mission));
    }

    @Test
    void testCurseValidator_Fail() {
        mission.getCurse().setName("");
        assertThrows(ValidationException.class, () -> new CurseValidator().validate(mission));
        
        mission.setCurse(null);
        assertThrows(ValidationException.class, () -> new CurseValidator().validate(mission));
    }

    @Test
    void testSorcerersValidator_Fail() {
        mission.setSorcerers(null);
        assertThrows(ValidationException.class, () -> new SorcerersValidator().validate(mission));
        
        Sorcerer s = new Sorcerer();
        s.setName("");
        mission.setSorcerers(Collections.singletonList(s));
        assertThrows(ValidationException.class, () -> new SorcerersValidator().validate(mission));
    }

    @Test
    void testTechniquesValidator_Fail() {
        mission.setTechniques(new ArrayList<>());
        assertThrows(ValidationException.class, () -> new TechniquesValidator().validate(mission));

        Technique t = new Technique();
        t.setName("Fire");
        t.setOwner("Gojo");
        t.setType(null);
        mission.setTechniques(Collections.singletonList(t));
        assertThrows(ValidationException.class, () -> new TechniquesValidator().validate(mission));
    }

    @Test
    void testBusinessRulesValidator_WrongOwner() {
        mission.getTechniques().get(0).setOwner("Unknown Sorcerer");
        assertThrows(ValidationException.class, () -> new BusinessRulesValidator().validate(mission));
    }

    @Test
    void testBusinessRulesValidator_NegativeDamage() {
        mission.setDamageCost(-100);
        assertThrows(ValidationException.class, () -> new BusinessRulesValidator().validate(mission));
    }
}
