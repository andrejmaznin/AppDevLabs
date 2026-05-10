package adapters.out.io;

import domain.models.Mission;
import adapters.out.parsing.MissionParser;
import adapters.out.parsing.MissionParserFactory;
import ports.out.MissionSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class FileMissionSource implements MissionSource {
    private static final Logger logger = LoggerFactory.getLogger(FileMissionSource.class);
    private final File file;

    public FileMissionSource(File file) {
        this.file = file;
    }

    @Override
    public List<Mission> loadMissions() throws Exception {
        if (file == null || !file.exists()) {
            logger.error("Файл не найден или не существует: {}", (file != null ? file.getPath() : "null"));
            throw new IllegalArgumentException("Файл не выбран или не существует: " + (file != null ? file.getPath() : "null"));
        }

        logger.info("Чтение данных из файла: {}", file.getName());
        try {
            String content = Files.readString(file.toPath());
            MissionParser parser = MissionParserFactory.getParserByFileName(file.getName());
            Mission mission = parser.parse(content);

            if (mission != null) {
                logger.debug("Файл {} успешно прочитан и распарсен", file.getName());
                return List.of(mission);
            } else {
                logger.warn("Парсер вернул пустой результат для файла: {}", file.getName());
                throw new Exception("Не удалось распарсить файл: " + file.getName());
            }
        } catch (Exception e) {
            logger.error("Ошибка при обработке файла {}: {}", file.getName(), e.getMessage());
            throw e;
        }
    }
}
