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
    @Query(value = "select count(*) from pp_slot where leaderid = :leaderID and dayid = :dayID and ( (to_char(starttime + INTERVAL '5' MINUTE, 'HH24:MI:SS') < to_char(:startTime, 'HH24:MI:SS') and to_char(endtime - INTERVAL '5' MINUTE, 'HH24:MI:SS') > to_char(:startTime, 'HH24:MI:SS')) or (to_char(starttime + INTERVAL '5' MINUTE, 'HH24:MI:SS') < to_char(:endTime, 'HH24:MI:SS') and to_char(endtime - INTERVAL '5' MINUTE, 'HH24:MI:SS') > to_char(:endTime, 'HH24:MI:SS')) or (to_char(starttime, 'HH24:MI:SS') >= to_char(:startTime, 'HH24:MI:SS') and to_char(endtime, 'HH24:MI:SS') <= to_char(:endTime, 'HH24:MI:SS')) ) ", nativeQuery = true)
    int sameSlotTimeFind(String leaderID, char dayID, Timestamp startTime, Timestamp endTime);
}
