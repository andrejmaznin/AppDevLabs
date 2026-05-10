package adapters.out.parsing;

import domain.models.Mission;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BinMissionParserTest {

    private final BinMissionParser parser = new BinMissionParser();

    @Test
    void testParse_ExampleMission() {
        String data = """
            MISSION_CREATED|M-2024-031|2024-12-02|Токио, район Асакуса
            CURSE_DETECTED|Проклятие красных фонарей|HIGH
            SORCERER_ASSIGNED|Маки Дзэнин|GRADE_2
            SORCERER_ASSIGNED|Тодо Аой|GRADE_1
            TECHNIQUE_USED|Разрушительный удар|BODY|Тодо Аой|650000
            TECHNIQUE_USED|Проклятый инструмент: копьё|WEAPON|Маки Дзэнин|420000
            TIMELINE_EVENT|2024-12-02T18:05:00|DETECTION|Обнаружен источник проклятой энергии
            TIMELINE_EVENT|2024-12-02T18:11:00|ENGAGEMENT|Начало прямого столкновения
            TIMELINE_EVENT|2024-12-02T18:19:00|CIVILIAN_EVACUATION|Начата эвакуация гражданских
            ENEMY_ACTION|DIRECT_ASSAULT|атака по фронту
            ENEMY_ACTION|TRAP_USAGE|использование замкнутого пространства
            CIVILIAN_IMPACT|evacuated=43|injured=2|missing=0
            MISSION_RESULT|SUCCESS|damageCost=2100000
            """;

        Mission mission = parser.parse(data);

        assertNotNull(mission);
        assertEquals("M-2024-031", mission.getMissionId());
        assertEquals("Токио, район Асакуса", mission.getLocation());
        assertEquals("SUCCESS", mission.getOutcome());
        assertEquals(2100000L, mission.getDamageCost());
        assertNotNull(mission.getCurse());
        assertEquals("Проклятие красных фонарей", mission.getCurse().getName());
        assertEquals(2, mission.getSorcerers().size());
        assertEquals(2, mission.getTechniques().size());
        assertNotNull(mission.getCivilianImpact());
        assertEquals(43, mission.getCivilianImpact().getEvacuated());
        assertNotNull(mission.getEnemyActivity());
        assertEquals(2, mission.getEnemyActivity().getAttackPatterns().size());
        assertNotNull(mission.getOperationTimeline());
        assertEquals(3, mission.getOperationTimeline().size());
    }

    @Test
    void testParse_InvalidLine() {
        String data = "INVALID_COMMAND|some|data";
        // It should just ignore or not throw if not handled, but let's check code
        // case "MISSION_CREATED" ... default: (nothing)
        // Actually there is no default, so it just does nothing for unknown commands.
        // But if parts are missing, it might throw.
        
        String invalidData = "MISSION_CREATED|OnlyOnePart";
        assertThrows(RuntimeException.class, () -> parser.parse(invalidData));
    }
}
