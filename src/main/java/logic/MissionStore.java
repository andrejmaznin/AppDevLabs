package logic;

import models.Mission;
import specifications.MissionSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


public class MissionStore {
    private final MissionRepository repository;
    private final CopyOnWriteArrayList<MissionStoreListener> listeners = new CopyOnWriteArrayList<>();

    public MissionStore() {
        this(new InMemoryMissionRepository());
    }

    public MissionStore(MissionRepository repository) {
        this.repository = repository != null ? repository : new InMemoryMissionRepository();
    }

    public synchronized void add(Mission mission) {
        if (mission == null) {
            throw new IllegalArgumentException("Миссия не может быть null");
        }
        repository.save(mission);
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
