package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Slot;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;

import java.util.List;
import java.util.Optional;

public interface SlotRepo extends JpaRepository<Slot, Integer>
{
    List<Slot> findSlotsByLeader(User leader);

    Optional<Slot> findSlotByLeader_UseridAndSlotType_Typeid(String leaderID, char typeID);
}
