package polytechnic.bh.PassPlatforms_Backend.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import polytechnic.bh.PassPlatforms_Backend.Entity.Slot;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface SlotRepo extends JpaRepository<Slot, Integer>
{
    List<Slot> findSlotsByLeader_Userid(String leaderID);

    Optional<Slot> findSlotByLeader_UseridAndSlotType_Typeid(String leaderID, char typeID);

    // check if leader already has a slot within this time range
    @Transactional
    @Query(value = "SELECT count(*) FROM pp_slot " +
            "WHERE leaderid = :leaderID " +
            "AND dayid = :dayID " +
            "AND ( " +
            "  (CAST(starttime AS time) + INTERVAL '5' minute < CAST(:startTime AS time) AND CAST(endtime AS time) - INTERVAL '5' minute > CAST(:startTime AS time)) " +
            "  OR " +
            "  (CAST(starttime AS time) + INTERVAL '5' minute < CAST(:endTime AS time) AND CAST(endtime AS time) - INTERVAL '5' minute > CAST(:endTime AS time)) " +
            "  OR " +
            "  (CAST(starttime AS time) >= CAST(:startTime AS time) AND CAST(endtime AS time) <= CAST(:endTime AS time)) " +
            ")", nativeQuery = true)
    int sameSlotTimeFind(String leaderID, char dayID, Timestamp startTime, Timestamp endTime);
}
