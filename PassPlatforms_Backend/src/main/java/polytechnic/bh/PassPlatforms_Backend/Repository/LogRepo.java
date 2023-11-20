package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Log;

import java.util.Date;
import java.util.List;

public interface LogRepo extends JpaRepository<Log, Integer>
{
    List<Log> findLogsByErrormsgContainsIgnoreCase(String error);

    List<Log> findLogsByDatetime(Date date);
}
