package logic;

import models.Mission;
import parsing.MissionParser;
import parsing.MissionParserFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class GUIEngine {
    private final MissionStore store;

    public GUIEngine() {
        this(new MissionStore());
    }

    public GUIEngine(MissionStore store) {
        this.store = store == null ? new MissionStore() : store;
    }

    public MissionStore getStore() {
        return store;
    }

    public void importMissions(io.MissionSource source) throws Exception {
        if (source == null) {
            throw new IllegalArgumentException("Источник данных не может быть null");
        }
        List<Mission> missions = source.loadMissions();
        for (Mission mission : missions) {
            store.add(mission);
        }
    }

    public void importMission(File file) throws Exception {
        importMissions(new io.FileMissionSource(file));
    }


    public List<Mission> getAllMissions() {
        return store.getAll();
    }

    public Optional<Mission> findMissionById(String id) {
        return store.findById(id);
    }

    public void clearMissions() {
        store.clear();
    }
}