package application;

import ports.in.MissionStoreListener;
import ports.out.MissionRepository;
import domain.models.Mission;
import domain.specifications.MissionSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MissionStoreTest {

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private MissionStoreListener listener;

    @Mock
    private MissionSpecification specification;

    @InjectMocks
    private MissionStore missionStore;

    private Mission mission1;
    private Mission mission2;

    @BeforeEach
    void setUp() {
        mission1 = new Mission();
        mission1.setMissionId("ID-1");
        mission2 = new Mission();
        mission2.setMissionId("ID-2");
    }

    @Test
    void testAdd_NullMission() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            missionStore.add(null);
        });

        assertEquals("Миссия не может быть null", exception.getMessage());
        verifyNoInteractions(missionRepository);
    }

    @Test
    void testAdd_Success() {
        missionStore.addListener(listener);
        missionStore.add(mission1);

        verify(missionRepository, times(1)).save(mission1);
        verify(listener, times(1)).onStoreChanged();
    }

    @Test
    void testAddAll_NullOrEmpty() {
        missionStore.addListener(listener);

        missionStore.addAll(null);
        missionStore.addAll(Collections.emptyList());

        verifyNoInteractions(missionRepository);
        verifyNoInteractions(listener);
    }

    @Test
    void testAddAll_Success() {
        missionStore.addListener(listener);
        List<Mission> missions = Arrays.asList(mission1, mission2);

        missionStore.addAll(missions);

        verify(missionRepository, times(1)).saveAll(missions);
        verify(listener, times(1)).onStoreChanged();
    }

    @Test
    void testGetAll() {
        List<Mission> expected = Arrays.asList(mission1, mission2);
        when(missionRepository.findAll()).thenReturn(expected);

        List<Mission> actual = missionStore.getAll();

        assertEquals(expected, actual);
        verify(missionRepository, times(1)).findAll();
    }

    @Test
    void testClear() {
        missionStore.addListener(listener);

        missionStore.clear();

        verify(missionRepository, times(1)).deleteAll();
        verify(listener, times(1)).onStoreChanged();
    }

    @Test
    void testGetByIndex() {
        List<Mission> list = Arrays.asList(mission1, mission2);
        when(missionRepository.findAll()).thenReturn(list);

        Mission actual = missionStore.get(1);

        assertEquals(mission2, actual);
    }

    @Test
    void testSize() {
        List<Mission> list = Arrays.asList(mission1, mission2);
        when(missionRepository.findAll()).thenReturn(list);

        int size = missionStore.size();

        assertEquals(2, size);
    }

    @Test
    void testFind_WithSpecification() {
        List<Mission> allMissions = Arrays.asList(mission1, mission2);
        when(missionRepository.findAll()).thenReturn(allMissions);
        when(specification.isSatisfiedBy(mission1)).thenReturn(true);
        when(specification.isSatisfiedBy(mission2)).thenReturn(false);

        List<Mission> found = missionStore.find(specification);

        assertEquals(1, found.size());
        assertEquals(mission1, found.get(0));
    }

    @Test
    void testFind_NullSpecification() {
        List<Mission> allMissions = Arrays.asList(mission1, mission2);
        when(missionRepository.findAll()).thenReturn(allMissions);

        List<Mission> found = missionStore.find(null);

        assertEquals(allMissions, found);
        assertEquals(2, found.size());
    }

    @Test
    void testFindById() {
        when(missionRepository.findById("ID-1")).thenReturn(Optional.of(mission1));

        Optional<Mission> found = missionStore.findById("ID-1");

        assertTrue(found.isPresent());
        assertEquals(mission1, found.get());
    }

    @Test
    void testRemoveListener() {
        missionStore.addListener(listener);
        missionStore.removeListener(listener);
        missionStore.add(mission1);

        verify(missionRepository, times(1)).save(mission1);
        verifyNoInteractions(listener); // onStoreChanged shouldn't be called
    }
}
