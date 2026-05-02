package adapters.in.rest;

import application.MissionStore;
import application.ports.in.MissionUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.models.Curse;
import domain.models.Mission;
import domain.models.Sorcerer;
import domain.models.Technique;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MissionRestController.class)
@ContextConfiguration(classes = {MissionRestControllerTest.TestApp.class, MissionRestController.class, MissionRestControllerTest.GlobalExceptionHandler.class})
class MissionRestControllerTest {

    @SpringBootApplication
    static class TestApp {}

    @ControllerAdvice
    static class GlobalExceptionHandler {
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> handleException(RuntimeException ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MissionUseCase missionUseCase;

    @MockBean
    private MissionStore missionStore;

    private Mission mission1;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mission1 = new Mission();
        mission1.setMissionId("ID-123");
        mission1.setLocation("Moscow");
        mission1.setDate("2026-05-02");
        mission1.setOutcome("SUCCESS");
        Curse curse = new Curse();
        curse.setName("Test Curse");
        curse.setThreatLevel("GRADE_1");
        mission1.setCurse(curse);
        Sorcerer sorcerer = new Sorcerer();
        sorcerer.setName("Test Sorcerer");
        sorcerer.setRank("GRADE_1");
        mission1.setSorcerers(Arrays.asList(sorcerer));
        Technique technique = new Technique();
        technique.setName("Test Technique");
        technique.setOwner("Test Sorcerer");
        technique.setType("SUPPORT");
        mission1.setTechniques(Arrays.asList(technique));

        when(missionUseCase.getStore()).thenReturn(missionStore);
    }

    @Test
    void testGetMissions_WithoutSearch() throws Exception {
        when(missionUseCase.getAllMissions()).thenReturn(Arrays.asList(mission1));

        mockMvc.perform(get("/api/missions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].missionId").value("ID-123"))
                .andExpect(jsonPath("$[0].location").value("Moscow"));

        verify(missionUseCase, times(1)).getAllMissions();
    }

    @Test
    void testGetMissions_WithSearch() throws Exception {
        when(missionStore.find(any())).thenReturn(Arrays.asList(mission1));

        mockMvc.perform(get("/api/missions").param("search", "Moscow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].missionId").value("ID-123"));

        verify(missionStore, times(1)).find(any());
        verify(missionUseCase, never()).getAllMissions();
    }

    @Test
    void testGetMissionById_Found() throws Exception {
        when(missionUseCase.findMissionById("ID-123")).thenReturn(Optional.of(mission1));

        mockMvc.perform(get("/api/missions/ID-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.missionId").value("ID-123"));
    }

    @Test
    void testGetMissionById_NotFound() throws Exception {
        when(missionUseCase.findMissionById("UNKNOWN")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/missions/UNKNOWN"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Миссия не найдена: UNKNOWN"));
    }

    @Test
    void testAddMission() throws Exception {
        String json = objectMapper.writeValueAsString(mission1);

        mockMvc.perform(post("/api/missions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        verify(missionStore, times(1)).add(any(Mission.class));
    }

    @Test
    void testClearMissions() throws Exception {
        mockMvc.perform(delete("/api/missions"))
                .andExpect(status().isOk());

        verify(missionUseCase, times(1)).clearMissions();
    }

    @Test
    void testImportMission() throws Exception {
        String jsonContent = """
                {
                  "missionId": "M-001",
                  "date": "2026-05-02",
                  "location": "Tokyo",
                  "outcome": "SUCCESS",
                  "curse": {
                    "name": "Test Curse",
                    "threatLevel": "GRADE_1"
                  },
                  "sorcerers": [
                    {
                      "name": "Test Sorcerer",
                      "rank": "GRADE_1"
                    }
                  ],
                  "techniques": [
                    {
                      "name": "Test Technique",
                      "owner": "Test Sorcerer",
                      "type": "SUPPORT"
                    }
                  ]
                }
                """;
        MockMultipartFile file = new MockMultipartFile("file", "test.json", MediaType.APPLICATION_JSON_VALUE, jsonContent.getBytes());

        mockMvc.perform(multipart("/api/missions/import").file(file))
                .andExpect(status().isOk());

        verify(missionStore, times(1)).add(any(Mission.class));
    }
}
