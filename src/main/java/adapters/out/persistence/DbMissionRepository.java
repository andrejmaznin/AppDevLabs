package adapters.out.persistence;

import ports.out.MissionRepository;
import domain.models.Mission;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Primary
@Transactional
public class DbMissionRepository implements MissionRepository {

    private final JpaMissionRepository jpaRepository;

    public DbMissionRepository(JpaMissionRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Mission mission) {
        jpaRepository.save(MissionEntity.fromDomain(mission));
    }

    @Override
    public void saveAll(List<Mission> missions) {
        List<MissionEntity> entities = missions.stream()
                .map(MissionEntity::fromDomain)
                .collect(Collectors.toList());
        jpaRepository.saveAll(entities);
    }

    @Override
    public List<Mission> findAll() {
        return jpaRepository.findAll().stream()
                .map(MissionEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Mission> findById(String id) {
        return jpaRepository.findById(id)
                .map(MissionEntity::toDomain);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
