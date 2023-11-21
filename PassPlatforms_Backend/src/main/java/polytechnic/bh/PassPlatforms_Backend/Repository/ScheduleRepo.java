package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Schedule;

import java.util.List;

public interface ScheduleRepo extends JpaRepository<Schedule, Integer>
{
    List<Schedule> findAllByUser_Userid(String userID);
}
