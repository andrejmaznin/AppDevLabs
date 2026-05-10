package adapters.out.parsing;

import domain.models.Mission;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonMissionParserTest {

    private final JsonMissionParser parser = new JsonMissionParser();

    @Test
    void testParse_ValidJson() {
        String json = """
                {
                  "missionId": "M-001",
                  "date": "2026-05-02",
                  "location": "Tokyo",
                  "outcome": "SUCCESS",
                  "damageCost": 1000,
                  "curse": {
                    "name": "Ryomen Sukuna",
                    "threatLevel": "SPECIAL_GRADE"
                  },
                  "sorcerers": [
                    {
                      "name": "Gojo Satoru",
                      "rank": "SPECIAL_GRADE"
                    }
                  ],
                  "techniques": [
                    {
                      "name": "Hollow Purple",
                      "type": "OFFENSIVE",
                      "energyCost": 500,
                      "owner": "Gojo Satoru"
                    }
                  ]
                }
                """;

        Mission mission = parser.parse(json);

        assertNotNull(mission);
        assertEquals("M-001", mission.getMissionId());
        assertEquals("2026-05-02", mission.getDate());
        assertEquals("Tokyo", mission.getLocation());
        assertEquals("SUCCESS", mission.getOutcome());
        assertEquals(1000, mission.getDamageCost());
    }

    @Test
    void testParse_InvalidJson() {
        String invalidJson = "{ invalid json ";

        Exception exception = assertThrows(RuntimeException.class, () -> {
            parser.parse(invalidJson);
        });

        assertTrue(exception.getMessage().contains("Ошибка JSON"));
    }
}
