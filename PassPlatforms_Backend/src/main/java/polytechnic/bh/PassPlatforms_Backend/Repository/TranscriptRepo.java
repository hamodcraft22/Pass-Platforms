package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Transcript;

public interface TranscriptRepo extends JpaRepository<Transcript, Integer>
{
}
