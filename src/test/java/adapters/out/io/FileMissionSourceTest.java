package adapters.out.io;

import domain.models.Mission;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileMissionSourceTest {

    @TempDir
    Path tempDir;

    @Test
    void testLoadMissions_Success() throws Exception {
        // Create a valid JSON mission file
        Path filePath = tempDir.resolve("test.json");
        String json = """
                {
                  "missionId": "M-IO",
                  "date": "2024-05-02",
                  "location": "IO-Test",
                  "outcome": "SUCCESS",
                  "curse": { "name": "IO-Curse", "threatLevel": "LOW" },
                  "sorcerers": [{ "name": "IO-Mage", "rank": "GRADE_4" }],
                  "techniques": [{ "name": "IO-Tech", "type": "INNATE", "owner": "IO-Mage" }]
                }
                """;
        Files.writeString(filePath, json);

        FileMissionSource source = new FileMissionSource(filePath.toFile());
        List<Mission> missions = source.loadMissions();

        assertNotNull(missions);
        assertEquals(1, missions.size());
        assertEquals("M-IO", missions.get(0).getMissionId());
    }

    @Test
    void testLoadMissions_FileNotFound() {
        File nonExistentFile = new File("non_existent_file.json");
        FileMissionSource source = new FileMissionSource(nonExistentFile);

        assertThrows(IllegalArgumentException.class, source::loadMissions);
    }

    @Test
    void testLoadMissions_NullFile() {
        FileMissionSource source = new FileMissionSource(null);
        assertThrows(IllegalArgumentException.class, source::loadMissions);
    }

    @Test
    void testLoadMissions_InvalidContent() throws Exception {
        Path filePath = tempDir.resolve("invalid.json");
        Files.writeString(filePath, "invalid content");

        FileMissionSource source = new FileMissionSource(filePath.toFile());
        // Should throw RuntimeException from JsonMissionParser wrapped in Exception
        assertThrows(Exception.class, source::loadMissions);
    }
}
