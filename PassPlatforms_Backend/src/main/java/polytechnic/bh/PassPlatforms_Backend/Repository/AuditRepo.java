package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Audit;

public interface AuditRepo extends JpaRepository<Audit, Integer>
{

}
