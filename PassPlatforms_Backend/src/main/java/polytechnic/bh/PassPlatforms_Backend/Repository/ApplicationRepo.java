package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Application;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;

public interface ApplicationRepo extends JpaRepository<Application, Integer> {
    Application findApplicationByUser(User user);
}
