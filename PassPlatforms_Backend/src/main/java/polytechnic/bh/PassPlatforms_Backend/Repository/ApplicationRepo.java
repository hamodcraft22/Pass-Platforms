package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Application;

import java.util.Optional;

public interface ApplicationRepo extends JpaRepository<Application, Integer>
{
    Optional<Application> findApplicationByUser_Userid(String userID);
}
