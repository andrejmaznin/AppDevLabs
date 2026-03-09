package logic;

import models.Mission;
import parsing.MissionParser;
import parsing.MissionParserFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Engine {
    private final List<Mission> missions;

    public Engine() {
        this.missions = new ArrayList<>();
    }

    public void importMission(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("Файл не выбран или не существует.");
        }

        String content = Files.readString(file.toPath());

        MissionParser parser = MissionParserFactory.getParserByFileName(file.getName());

        Mission mission = parser.parse(content);

        if (mission != null) {
            missions.add(mission);
        } else {
            throw new Exception("Не удалось распарсить файл: " + file.getName());
        }
    }

    public List<Mission> getAllMissions() {
        return Collections.unmodifiableList(missions);
    }

    public Optional<Mission> findMissionById(String id) {
        return missions.stream()
            .filter(m -> m.getMissionId().equals(id))
            .findFirst();
    }

    public void clearMissions() {
        missions.clear();
    }
}