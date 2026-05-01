package application;

import domain.models.Mission;
import application.ports.in.MissionUseCase;
import application.ports.out.MissionSource;
import adapters.out.io.FileMissionSource;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class AppEngine implements MissionUseCase {
    private final MissionStore store;

    public AppEngine(MissionStore store) {
        this.store = store;
    }

    @Override
    public MissionStore getStore() {
        return store;
    }

    @Override
    public void importMissions(MissionSource source) throws Exception {
        if (source == null) {
            throw new IllegalArgumentException("Источник данных не может быть null");
        }
        List<Mission> missions = source.loadMissions();
        store.addAll(missions);
    }

    @Override
    public void importMission(File file) throws Exception {
        importMissions(new FileMissionSource(file));
    }

    @Override
    public List<Mission> getAllMissions() {
        return store.getAll();
    }

    @Override
    public Optional<Mission> findMissionById(String id) {
        return store.findById(id);
    }

    @Override
    public void clearMissions() {
        store.clear();
    }
}
