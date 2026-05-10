package adapters.out.parsing;

import domain.models.Mission;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TxtMissionParserTest {

    private final TxtMissionParser parser = new TxtMissionParser();

    @Test
    void testParse_ExampleMission() {
        String txt = """
            [MISSION]
            missionId=M-2024-028
            date=2024-11-24
            location=Токио, район Икэбукуро
            outcome=SUCCESS
            damageCost=1600000
            
            [CURSE]
            name=Проклятие ливневого коллектора
            threatLevel=HIGH
            
            [SORCERER]
            name=Маки Дзэнин
            rank=GRADE_2
            
            [SORCERER]
            name=Итадори Юдзи
            rank=GRADE_1
            
            [TECHNIQUE]
            name=Проклятый инструмент: нагината
            type=WEAPON
            owner=Маки Дзэнин
            damage=600000
            
            [TECHNIQUE]
            name=Черная вспышка
            type=INNATE
            owner=Итадори Юдзи
            damage=750000
            
            [ENVIRONMENT]
            weather=HEAVY_RAIN
            timeOfDay=NIGHT
            visibility=LOW
            cursedEnergyDensity=87
            """;

        Mission mission = parser.parse(txt);

        assertNotNull(mission);
        assertEquals("M-2024-028", mission.getMissionId());
        assertEquals("Токио, район Икэбукуро", mission.getLocation());
        assertEquals("SUCCESS", mission.getOutcome());
        assertEquals(1600000L, mission.getDamageCost());
        assertNotNull(mission.getCurse());
        assertEquals("Проклятие ливневого коллектора", mission.getCurse().getName());
        assertEquals(2, mission.getSorcerers().size());
        assertEquals(2, mission.getTechniques().size());
        assertNotNull(mission.getEnvironmentConditions());
        assertEquals("HEAVY_RAIN", mission.getEnvironmentConditions().getWeather());
    }
}
