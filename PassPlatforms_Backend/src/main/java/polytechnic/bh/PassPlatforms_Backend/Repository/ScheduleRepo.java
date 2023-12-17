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
    @Query(value = "SELECT count(*) FROM pp_schedule WHERE userid = :userID and dayid = :dayID and ( (to_char(starttime + INTERVAL '5' MINUTE, 'HH24:MI:SS') < to_char(:startTime, 'HH24:MI:SS') and to_char(endtime - INTERVAL '5' MINUTE, 'HH24:MI:SS') > to_char(:startTime, 'HH24:MI:SS')) or (to_char(starttime + INTERVAL '5' MINUTE, 'HH24:MI:SS') < to_char(:endTime, 'HH24:MI:SS') and to_char(endtime - INTERVAL '5' MINUTE, 'HH24:MI:SS') > to_char(:endTime, 'HH24:MI:SS')) or (to_char(starttime, 'HH24:MI:SS') >= to_char(:startTime, 'HH24:MI:SS') and to_char(endtime, 'HH24:MI:SS') <= to_char(:endTime, 'HH24:MI:SS')) )", nativeQuery = true)
    int sameTimeClassesFind(String userID, char dayID, Timestamp startTime, Timestamp endTime);
}
