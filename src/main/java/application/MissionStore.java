package application;

import domain.models.Mission;
import domain.specifications.MissionSpecification;
import application.ports.out.MissionRepository;
import application.ports.in.MissionStoreListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class MissionStore {
    private static final Logger logger = LoggerFactory.getLogger(MissionStore.class);
    private final MissionRepository repository;
    private final CopyOnWriteArrayList<MissionStoreListener> listeners = new CopyOnWriteArrayList<>();

    public MissionStore(MissionRepository repository) {
        this.repository = repository;
    }

    public synchronized void add(Mission mission) {
        if (mission == null) {
            logger.error("Попытка добавления пустой миссии");
            throw new IllegalArgumentException("Миссия не может быть null");
        }
        logger.info("Добавление новой миссии: ID={}", mission.getMissionId());
        repository.save(mission);
        notifyListeners();
    }

    public synchronized void addAll(List<Mission> missions) {
        if (missions == null || missions.isEmpty()) {
            logger.debug("Список миссий для добавления пуст");
            return;
        }
        logger.info("Массовое добавление миссий: количество={}", missions.size());
        repository.saveAll(missions);
        notifyListeners();
    }

    public synchronized List<Mission> getAll() {
        return repository.findAll();
    }

    public synchronized void clear() {
        logger.info("Очистка всех данных в репозитории");
        repository.deleteAll();
        notifyListeners();
    }

    public synchronized Mission get(int index) {
        return repository.findAll().get(index);
    }

    public synchronized int size() {
        return repository.findAll().size();
    }

    public void addListener(MissionStoreListener listener) {
        if (listener == null) {
            return;
        }
        logger.debug("Добавлен слушатель изменений: {}", listener.getClass().getSimpleName());
        listeners.addIfAbsent(listener);
    }

    public void removeListener(MissionStoreListener listener) {
        if (listener == null) return;
        logger.debug("Удален слушатель изменений: {}", listener.getClass().getSimpleName());
        listeners.remove(listener);
    }

    private void notifyListeners() {
        logger.trace("Уведомление слушателей об изменении хранилища (всего: {})", listeners.size());
        for (MissionStoreListener l : listeners) {
            try {
                l.onStoreChanged();
            } catch (Exception e) {
                logger.error("Ошибка при уведомлении слушателя {}: {}", l.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    public synchronized List<Mission> find(MissionSpecification specification) {
        logger.debug("Поиск миссий по спецификации: {}", (specification != null ? specification.getClass().getSimpleName() : "ALL"));
        List<Mission> all = repository.findAll();
        if (specification == null) {
            return new ArrayList<>(all);
        }
        return all.stream()
            .filter(specification::isSatisfiedBy)
            .collect(Collectors.toList());
    }

    public synchronized Optional<Mission> findById(String id) {
        return repository.findById(id);
    }
}
