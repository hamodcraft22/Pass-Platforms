package polytechnic.bh.PassPlatforms_Backend.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import polytechnic.bh.PassPlatforms_Backend.Entity.Schedule;

import java.sql.Timestamp;
import java.util.List;

public interface ScheduleRepo extends JpaRepository<Schedule, Integer>
{
    List<Schedule> findAllByUser_Userid(String userID);

    @Transactional
    @Query(value = "SELECT count(*) FROM pp_schedule " +
            "WHERE userid = :userID " +
            "AND dayid = :dayID " +
            "AND ( " +
            "  (CAST(starttime AS time) + INTERVAL '5' minute < CAST(:startTime AS time) " +
            "   AND CAST(endtime AS time) - INTERVAL '5' minute > CAST(:startTime AS time)) " +
            "  OR " +
            "  (CAST(starttime AS time) + INTERVAL '5' minute < CAST(:endTime AS time) " +
            "   AND CAST(endtime AS time) - INTERVAL '5' minute > CAST(:endTime AS time)) " +
            "  OR " +
            "  (CAST(starttime AS time) >= CAST(:startTime AS time) AND CAST(endtime AS time) <= CAST(:endTime AS time)) " +
            ")", nativeQuery = true)
    int sameTimeClassesFind(String userID, char dayID, Timestamp startTime, Timestamp endTime);
}
