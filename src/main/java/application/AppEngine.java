package application;

import domain.models.Mission;
import ports.in.MissionUseCase;
import ports.out.MissionSource;
import adapters.out.io.FileMissionSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class AppEngine implements MissionUseCase {
    private static final Logger logger = LoggerFactory.getLogger(AppEngine.class);
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
            logger.error("Попытка импорта с пустым источником данных");
            throw new IllegalArgumentException("Источник данных не может быть null");
        }
        logger.info("Начало импорта миссий из источника: {}", source.getClass().getSimpleName());
        try {
            List<Mission> missions = source.loadMissions();
            store.addAll(missions);
            logger.info("Успешно импортировано {} миссий", missions.size());
        } catch (Exception e) {
            logger.error("Ошибка при импорте миссий: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void importMission(File file) throws Exception {
        if (file == null || !file.exists()) {
            logger.warn("Файл для импорта не найден: {}", (file != null ? file.getPath() : "null"));
            return;
        }
        logger.debug("Импорт одиночной миссии из файла: {}", file.getName());
        importMissions(new FileMissionSource(file));
    }

    @Override
    public List<Mission> getAllMissions() {
        logger.debug("Запрос всех миссий из хранилища");
        return store.getAll();
    }

    @Override
    public Optional<Mission> findMissionById(String id) {
        logger.debug("Поиск миссии по ID: {}", id);
        return store.findById(id);
    }

    @Override
    public void clearMissions() {
        logger.warn("Выполнена полная очистка хранилища миссий");
        store.clear();
    }
}
