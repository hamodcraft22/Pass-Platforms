package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.RecStatus;

public interface RecStatusRepo extends JpaRepository<RecStatus, Character>
{
}
