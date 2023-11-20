package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.MemberStatus;

public interface MemberStatusRepo extends JpaRepository<MemberStatus, Character>
{
}
