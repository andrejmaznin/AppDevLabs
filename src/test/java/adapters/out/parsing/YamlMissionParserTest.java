package adapters.out.parsing;

import domain.models.Mission;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class YamlMissionParserTest {

    private final YamlMissionParser parser = new YamlMissionParser();

    @Test
    void testParse_ExampleMission() {
        String yaml = """
            missionId: M-2024-021
            date: 2024-11-03
            location: Токио, район Гиндза
            outcome: SUCCESS
            curse:
              name: Проклятие стеклянной галереи
              threatLevel: SPECIAL_GRADE
            sorcerers:
              - name: Кугисаки Нобара
                rank: GRADE_2
              - name: Нанами Кэнто
                rank: GRADE_1
            techniques:
              - name: Соломенная кукла
                type: INNATE
                owner: Кугисаки Нобара
                damage: 250000
              - name: Коэффициент 7:3
                type: INNATE
                owner: Нанами Кэнто
                damage: 980000
            """;

        Mission mission = parser.parse(yaml);

        assertNotNull(mission);
        assertEquals("M-2024-021", mission.getMissionId());
        assertEquals("Токио, район Гиндза", mission.getLocation());
        assertEquals("SUCCESS", mission.getOutcome());
        assertNotNull(mission.getCurse());
        assertEquals("Проклятие стеклянной галереи", mission.getCurse().getName());
        assertEquals(2, mission.getSorcerers().size());
        assertEquals(2, mission.getTechniques().size());
    }

    @Test
    void testParse_InvalidYaml() {
        String invalidYaml = "missionId: : invalid";
        assertThrows(RuntimeException.class, () -> parser.parse(invalidYaml));
    }
}
