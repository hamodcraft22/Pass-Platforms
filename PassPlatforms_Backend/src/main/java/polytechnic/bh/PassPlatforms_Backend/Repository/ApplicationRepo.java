package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Application;
import polytechnic.bh.PassPlatforms_Backend.Entity.ApplicationStatus;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;

import java.util.Date;
import java.util.List;

public interface ApplicationRepo extends JpaRepository<Application, Integer> {
    List<Application> findApplicationsByDatetime (Date dateTime);
    List<Application> findApplicationsByNoteContainsIgnoreCase(String note);
    List<Application> findApplicationsByApplicationStatus(ApplicationStatus status);
    List<Application> findApplicationsByUser(User user);
}