package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.ApplicationStatus;

public interface ApplicationStatusRepo extends JpaRepository<ApplicationStatus, Integer> {
}
