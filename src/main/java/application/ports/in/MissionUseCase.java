package application.ports.in;

import domain.models.Mission;
import application.MissionStore;
import application.ports.out.MissionSource;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface MissionUseCase {
    void importMissions(MissionSource source) throws Exception;
    void importMission(File file) throws Exception;
    List<Mission> getAllMissions();
    Optional<Mission> findMissionById(String id);
    void clearMissions();
    MissionStore getStore();
}
