package domain.builder;

import domain.models.Curse;
import domain.models.Mission;
import domain.models.Sorcerer;
import domain.models.Technique;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BuilderTest {

    @Test
    void testStandardMissionBuilder() {
        StandardMissionBuilder builder = new StandardMissionBuilder();
        
        Curse curse = new Curse();
        curse.setName("Hanami");
        curse.setThreatLevel("SPECIAL");

        Sorcerer s = new Sorcerer();
        s.setName("Itadori");
        s.setRank("GRADE_1");

        Technique t = new Technique();
        t.setName("Divergent Fist");
        t.setOwner("Itadori");
        t.setType("PHYSICAL");

        Mission mission = builder.setMissionId("M-BUILD")
            .setDate("2024-05-02")
            .setLocation("School")
            .setOutcome("SUCCESS")
            .setCurse(curse)
            .addSorcerer(s)
            .addTechnique(t)
            .build();

        assertEquals("M-BUILD", mission.getMissionId());
        assertEquals("Hanami", mission.getCurse().getName());
        assertEquals(1, mission.getSorcerers().size());
        assertEquals("Itadori", mission.getSorcerers().get(0).getName());
    }

    @Test
    void testMissionBuildDirector() {
        MissionData data = new MissionData();
        data.missionId = "DIR-1";
        data.date = "2024-05-02";
        data.location = "Kyoto";
        data.outcome = "SUCCESS";
        data.curseName = "Jogo";
        data.curseThreatLevel = "SPECIAL";
        
        Sorcerer s = new Sorcerer();
        s.setName("Gojo");
        s.setRank("SPECIAL");
        data.sorcerers = new ArrayList<>();
        data.sorcerers.add(s);

        Technique t = new Technique();
        t.setName("Infinity");
        t.setOwner("Gojo");
        t.setType("BARRIER");
        data.techniques = new ArrayList<>();
        data.techniques.add(t);

        MissionBuildDirector director = new MissionBuildDirector(new StandardMissionBuilder());
        Mission mission = director.construct(data);

        assertEquals("DIR-1", mission.getMissionId());
        assertEquals("Jogo", mission.getCurse().getName());
        assertTrue(mission.getSorcerers().stream().anyMatch(sr -> sr.getName().equals("Gojo")));
    }
}
