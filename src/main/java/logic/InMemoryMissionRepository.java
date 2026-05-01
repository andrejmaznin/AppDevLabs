package logic;

import models.Mission;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;


public class InMemoryMissionRepository implements MissionRepository {
    private final List<Mission> missions = new CopyOnWriteArrayList<>();

    @Override
    public void save(Mission mission) {
        if (mission != null) {
            missions.add(mission);
        }
    }

    @Override
    public void saveAll(List<Mission> missions) {
        if (missions != null) {
            this.missions.addAll(missions);
        }
    }

    @Override
    public List<Mission> findAll() {
        return new ArrayList<>(missions);
    }

    @Override
    public Optional<Mission> findById(String id) {
        return missions.stream().filter(m -> m.getMissionId() != null && m.getMissionId().equals(id)).findFirst();
    }

    @Override
    public void deleteAll() {
        missions.clear();
    }

    @Override
    public void deleteById(String id) {
        missions.removeIf(m -> m.getMissionId() != null && m.getMissionId().equals(id));
    }
}
