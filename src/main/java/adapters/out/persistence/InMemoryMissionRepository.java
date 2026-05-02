package adapters.out.persistence;

import domain.models.Mission;
import application.ports.out.MissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryMissionRepository implements MissionRepository {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryMissionRepository.class);
    private final List<Mission> missions = new CopyOnWriteArrayList<>();

    @Override
    public void save(Mission mission) {
        if (mission != null) {
            logger.debug("Хранилище (In-Memory): сохранение миссии {}", mission.getMissionId());
            missions.add(mission);
        }
    }

    @Override
    public void saveAll(List<Mission> missions) {
        if (missions != null) {
            logger.debug("Хранилище (In-Memory): сохранение списка миссий (кол-во: {})", missions.size());
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
        logger.info("Хранилище (In-Memory): удаление всех миссий");
        missions.clear();
    }

    @Override
    public void deleteById(String id) {
        logger.info("Хранилище (In-Memory): удаление миссии {}", id);
        missions.removeIf(m -> m.getMissionId() != null && m.getMissionId().equals(id));
    }
}
