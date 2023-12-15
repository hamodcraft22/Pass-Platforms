package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Schedule;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleDao
{
    private int scheduleid;
    private Instant starttime;
    private Instant endtime;
    private DayDao day;
    private UserDao user;

    public ScheduleDao(Schedule schedule)
    {
        this.scheduleid = schedule.getScheduleid();
        this.starttime = schedule.getStarttime().toInstant();
        this.endtime = schedule.getEndtime().toInstant();
        this.day = new DayDao(schedule.getDay());
        this.user = new UserDao(schedule.getUser().getUserid(), new RoleDao(schedule.getUser().getRole()), null);
    }
}
