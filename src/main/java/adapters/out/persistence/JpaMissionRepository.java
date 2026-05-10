package adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMissionRepository extends JpaRepository<MissionEntity, String> {
}
