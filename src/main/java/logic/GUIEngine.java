package logic;

import models.Mission;
import parsing.MissionParser;
import parsing.MissionParserFactory;
import gui.SmartMissionFrame;

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

    public void importMission(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("Файл не выбран или не существует.");
        }

        String content = Files.readString(file.toPath());

        MissionParser parser = MissionParserFactory.getParserByFileName(file.getName());

        Mission mission = parser.parse(content);

        if (mission != null) {
            store.add(mission);
        } else {
            throw new Exception("Не удалось распарсить файл: " + file.getName());
        }
    }

    public void openSmartFrame(Mission mission) {
        if (mission == null) throw new IllegalArgumentException("mission is null");
        SmartMissionFrame.showInFrame(mission);
    }

    public void openSmartFrameFromFile(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("Файл не выбран или не существует.");
        }
        String content = Files.readString(file.toPath());
        MissionParser parser = MissionParserFactory.getParserByFileName(file.getName());
        Mission mission = parser.parse(content);
        openSmartFrame(mission);
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