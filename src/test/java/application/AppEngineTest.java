package application;

import ports.out.MissionSource;
import domain.models.Mission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppEngineTest {

    @Mock
    private MissionStore missionStore;

    @Mock
    private MissionSource missionSource;

    @InjectMocks
    private AppEngine appEngine;

    private Mission testMission1;
    private Mission testMission2;

    @BeforeEach
    void setUp() {
        testMission1 = new Mission();
        testMission1.setMissionId("M-001");
        
        testMission2 = new Mission();
        testMission2.setMissionId("M-002");
    }

    @Test
    void testGetStore() {
        assertEquals(missionStore, appEngine.getStore());
    }

    @Test
    void testImportMissions_Success() throws Exception {
        List<Mission> missions = Arrays.asList(testMission1, testMission2);
        when(missionSource.loadMissions()).thenReturn(missions);

        appEngine.importMissions(missionSource);

        verify(missionSource, times(1)).loadMissions();
        verify(missionStore, times(1)).addAll(missions);
    }

    @Test
    void testImportMissions_NullSource() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appEngine.importMissions(null);
        });

        assertEquals("Источник данных не может быть null", exception.getMessage());
        verifyNoInteractions(missionStore);
    }

    @Test
    void testGetAllMissions() {
        List<Mission> expectedMissions = Arrays.asList(testMission1, testMission2);
        when(missionStore.getAll()).thenReturn(expectedMissions);

        List<Mission> actualMissions = appEngine.getAllMissions();

        assertEquals(expectedMissions, actualMissions);
        verify(missionStore, times(1)).getAll();
    }

    @Test
    void testFindMissionById_Found() {
        when(missionStore.findById("M-001")).thenReturn(Optional.of(testMission1));

        Optional<Mission> foundMission = appEngine.findMissionById("M-001");

        assertTrue(foundMission.isPresent());
        assertEquals(testMission1, foundMission.get());
        verify(missionStore, times(1)).findById("M-001");
    }

    @Test
    void testFindMissionById_NotFound() {
        when(missionStore.findById("M-999")).thenReturn(Optional.empty());

        Optional<Mission> foundMission = appEngine.findMissionById("M-999");

        assertFalse(foundMission.isPresent());
        verify(missionStore, times(1)).findById("M-999");
    }

    @Test
    void testClearMissions() {
        appEngine.clearMissions();

        verify(missionStore, times(1)).clear();
    }
}
