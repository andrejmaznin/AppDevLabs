package ports.out;

import domain.models.Mission;

import java.util.List;

public interface MissionSource {
    List<Mission> loadMissions() throws Exception;
}
