package application;

import domain.models.Mission;
import domain.specifications.MissionSpecification;
import application.ports.out.MissionRepository;
import application.ports.in.MissionStoreListener;
import adapters.out.persistence.InMemoryMissionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


import org.springframework.stereotype.Component;

@Component
public class MissionStore {
    private final MissionRepository repository;
    private final CopyOnWriteArrayList<MissionStoreListener> listeners = new CopyOnWriteArrayList<>();

    public MissionStore(MissionRepository repository) {
        this.repository = repository;
    }

    public synchronized void add(Mission mission) {
        if (mission == null) {
            throw new IllegalArgumentException("Миссия не может быть null");
        }
        repository.save(mission);
        notifyListeners();
    }

    public synchronized void addAll(List<Mission> missions) {
        if (missions == null || missions.isEmpty()) {
            return;
        }
        repository.saveAll(missions);
        notifyListeners();
    }

    public synchronized List<Mission> getAll() {
        return repository.findAll();
    }


    public synchronized void clear() {
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
        listeners.addIfAbsent(listener);
    }

    public void removeListener(MissionStoreListener listener) {
        if (listener == null) return;
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (MissionStoreListener l : listeners) {
            try {
                l.onStoreChanged();
            } catch (Exception ignored) {
            }
        }
    }

    public synchronized List<Mission> find(MissionSpecification specification) {
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
