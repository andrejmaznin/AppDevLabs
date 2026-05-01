package logic;

import models.Mission;

import java.util.List;
import java.util.Optional;


public interface MissionRepository {
    void save(Mission mission);

    void saveAll(List<Mission> missions);

    List<Mission> findAll();

    Optional<Mission> findById(String id);

    void deleteAll();

    void deleteById(String id);
}
