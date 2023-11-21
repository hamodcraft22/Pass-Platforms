package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Transcript;

import java.util.List;

public interface TranscriptRepo extends JpaRepository<Transcript, Integer>
{
    List<Transcript> findTranscriptsByStudent_Userid(String userID);
}
