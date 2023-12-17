package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.ScheduleDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Schedule;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Repository.DayRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.ScheduleRepo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServ
{

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private DayRepo dayRepo;

    @Autowired
    private UserServ userServ;

    // get all schedules
    public List<ScheduleDao> getAllSchedules()
    {
        List<ScheduleDao> schedules = new ArrayList<>();

        for (Schedule retrievedSchedule : scheduleRepo.findAll())
        {
            schedules.add(new ScheduleDao(retrievedSchedule));
        }

        return schedules;
    }

    // get all user schedule
    public List<ScheduleDao> getUserSchedules(String userID)
    {
        List<ScheduleDao> schedules = new ArrayList<>();

        for (Schedule retrievedSchedule : scheduleRepo.findAllByUser_Userid(userID))
        {
            schedules.add(new ScheduleDao(retrievedSchedule));
        }

        return schedules;
    }

    // get a single schedule
    public ScheduleDao getScheduleDetails(int scheduleID)
    {
        Optional<Schedule> retrievedSchedule = scheduleRepo.findById(scheduleID);

        return retrievedSchedule.map(ScheduleDao::new).orElse(null);
    }

    public ScheduleDao createSchedule(Instant startTime, Instant endTime, char dayID, String userID)
    {
        Schedule newSchedule = new Schedule();

        newSchedule.setStarttime(Timestamp.from(startTime));
        newSchedule.setEndtime(Timestamp.from(endTime));
        newSchedule.setDay(dayRepo.getReferenceById(dayID));
        newSchedule.setUser(new User(userServ.getUser(userID)));

        return new ScheduleDao(scheduleRepo.save(newSchedule));
    }

    public ScheduleDao editSchedule(ScheduleDao updatedSchedule)
    {
        Optional<Schedule> retrievedSchedule = scheduleRepo.findById(updatedSchedule.getScheduleid());

        if (retrievedSchedule.isPresent())
        {

            retrievedSchedule.get().setStarttime(Timestamp.from(updatedSchedule.getStarttime()));
            retrievedSchedule.get().setEndtime(Timestamp.from(updatedSchedule.getEndtime()));
            retrievedSchedule.get().setDay(dayRepo.getReferenceById(updatedSchedule.getDay().getDayid()));

            return new ScheduleDao(scheduleRepo.save(retrievedSchedule.get()));
        }
        else
        {
            return null;
        }
    }

    public boolean deleteSchedule(int scheduleID)
    {
        scheduleRepo.deleteById(scheduleID);
        return true;
    }
}

