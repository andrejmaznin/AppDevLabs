package logic;

import models.Mission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;


public class MissionStore {
    private final List<Mission> missions = new ArrayList<>();
    private final CopyOnWriteArrayList<MissionStoreListener> listeners = new CopyOnWriteArrayList<>();

    public synchronized void add(Mission mission) {
        if (mission == null) {
            throw new IllegalArgumentException("Миссия не может быть null");
        }
        missions.add(mission);
        notifyListeners();
    }

    public synchronized List<Mission> getAll() {
        return new ArrayList<>(missions);
    }

    public synchronized Optional<Mission> findById(String id) {
        if (id == null) return Optional.empty();
        return missions.stream().filter(m -> id.equals(m.getMissionId())).findFirst();
    }

    public synchronized void clear() {
        missions.clear();
        notifyListeners();
    }

    public synchronized Mission get(int index) {
        return missions.get(index);
    }

    public synchronized int size() {
        return missions.size();
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
}
