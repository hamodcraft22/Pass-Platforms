package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Schedule;

import java.sql.Timestamp;
import java.util.List;

public interface ScheduleRepo extends JpaRepository<Schedule, Integer>
{
    List<Schedule> findAllByUser_Userid(String userID);

    boolean existsByStarttimeBetweenAndUser_Userid(Timestamp startTime, Timestamp endTime, String userID);

    boolean existsByEndtimeBetweenAndUser_Userid(Timestamp startTime, Timestamp endTime, String userID);

    //TODO
    // combined start and end time check
}
