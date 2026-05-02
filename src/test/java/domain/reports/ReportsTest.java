package domain.reports;

import domain.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ReportsTest {

    private Mission mission;

    @BeforeEach
    void setUp() {
        mission = new Mission();
        mission.setMissionId("M-REP");
        mission.setDate("2024-05-02");
        mission.setLocation("Shibuya");
        mission.setOutcome("SUCCESS");
        mission.setDamageCost(500000);
        mission.setComment("Hard fight");

        Curse curse = new Curse();
        curse.setName("Mahito");
        curse.setThreatLevel("SPECIAL");
        mission.setCurse(curse);

        Sorcerer s = new Sorcerer();
        s.setName("Nanami");
        s.setRank("GRADE_1");
        mission.setSorcerers(Collections.singletonList(s));

        Technique t = new Technique();
        t.setName("Ratio");
        t.setOwner("Nanami");
        mission.setTechniques(Collections.singletonList(t));

        EnvironmentConditions env = new EnvironmentConditions();
        env.setWeather("Sunny");
        env.setTimeOfDay("Day");
        env.setVisibility("High");
        mission.setEnvironmentConditions(env);

        CivilianImpact civ = new CivilianImpact();
        civ.setEvacuated(100);
        civ.setInjured(5);
        mission.setCivilianImpact(civ);

        EnemyActivity enemy = new EnemyActivity();
        enemy.setBehaviorType("Aggressive");
        enemy.setMobility("High");
        mission.setEnemyActivity(enemy);

        OperationEvent event = new OperationEvent();
        event.setTimestamp("12:00");
        event.setType("START");
        event.setDescription("Mission started");
        mission.setOperationTimeline(Collections.singletonList(event));
    }

    @Test
    void testBasicReportStrategy() {
        BasicReportStrategy strategy = new BasicReportStrategy();
        BasicMissionReport report = strategy.generate(mission);

        assertNotNull(report);
        assertEquals("M-REP", report.getMissionId());
        assertTrue(report.getCurseName().contains("Mahito"));
        assertEquals(1, report.getSorcerersDetails().size());
        assertTrue(report.getSorcerersDetails().get(0).contains("Nanami"));
        assertTrue(report.getEnvironmentSummary().contains("Sunny"));
    }

    @Test
    void testTreeReportStrategy() {
        TreeReportStrategy strategy = new TreeReportStrategy();
        TreeReport report = strategy.generate(mission);

        assertNotNull(report);
        assertNotNull(report.getRootNode());
        // Root is "Mission (Mission)"
        assertTrue(report.getRootNode().getLabel().contains("Mission"));
        // Check children (Curse, Participants, etc)
        assertFalse(report.getRootNode().getChildren().isEmpty());
        
        // Find missionId child
        boolean foundId = report.getRootNode().getChildren().stream()
            .anyMatch(c -> c.getLabel().contains("missionId: M-REP"));
        assertTrue(foundId, "Should contain missionId child node");
    }

    @Test
    void testReportNavigator() {
        ReportNavigator navigator = new ReportNavigator(new BasicReportStrategy());
        Object result = navigator.execute(mission);
        assertTrue(result instanceof BasicMissionReport);
    }
}
