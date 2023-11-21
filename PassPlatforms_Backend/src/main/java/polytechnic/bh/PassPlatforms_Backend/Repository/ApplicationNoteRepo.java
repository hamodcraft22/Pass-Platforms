package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Application;
import polytechnic.bh.PassPlatforms_Backend.Entity.ApplicationNote;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;

import java.util.Date;
import java.util.List;

public interface ApplicationNoteRepo extends JpaRepository<ApplicationNote, Integer>
{
    List<ApplicationNote> findApplicationNotesByDatetime(Date datetime);

    List<ApplicationNote> findApplicationNoteByNotebodyContainingIgnoreCase(String noteBody);

    List<ApplicationNote> findApplicationNoteByApplication(Application application);

    List<ApplicationNote> findApplicationNoteByUser(User user);
}
