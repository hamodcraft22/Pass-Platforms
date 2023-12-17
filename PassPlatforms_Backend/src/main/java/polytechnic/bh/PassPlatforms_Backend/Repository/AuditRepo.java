package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Audit;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;

import java.util.Date;
import java.util.List;

public interface AuditRepo extends JpaRepository<Audit, Integer>
{

}
