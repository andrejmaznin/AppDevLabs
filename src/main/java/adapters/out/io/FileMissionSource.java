package adapters.out.io;

import domain.models.Mission;
import adapters.out.parsing.MissionParser;
import adapters.out.parsing.MissionParserFactory;
import application.ports.out.MissionSource;

import java.io.File;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class FileMissionSource implements MissionSource {
    private final File file;

    public FileMissionSource(File file) {
        this.file = file;
    }

    @Override
    public List<Mission> loadMissions() throws Exception {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("Файл не выбран или не существует: " + (file != null ? file.getPath() : "null"));
        }

        String content = Files.readString(file.toPath());
        MissionParser parser = MissionParserFactory.getParserByFileName(file.getName());
        Mission mission = parser.parse(content);

        if (mission != null) {
            return List.of(mission);
        } else {
            throw new Exception("Не удалось распарсить файл: " + file.getName());
        }
    }
}
