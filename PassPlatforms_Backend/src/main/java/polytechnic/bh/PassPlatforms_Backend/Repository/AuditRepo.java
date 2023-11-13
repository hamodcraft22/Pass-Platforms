package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Audit;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;

import java.util.Date;
import java.util.List;

public interface AuditRepo extends JpaRepository<Audit, Integer> {
    List<Audit> findAuditsByChangetype(char character);
    List<Audit> findAuditsByEntitynameContainsIgnoreCase(String entityName);
    List<Audit> findAuditsByDatetime(Date dateTime);
    List<Audit> findAuditsByUser(User user);

}
