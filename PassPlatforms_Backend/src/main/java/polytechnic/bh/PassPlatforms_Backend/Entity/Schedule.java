package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.ScheduleDao;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_schedule")
public class Schedule
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pp_schedule_SEQ")
    @SequenceGenerator(name = "pp_schedule_SEQ", sequenceName = "pp_schedule_SEQ", allocationSize = 1)
    private int scheduleid;
    private java.sql.Timestamp starttime;
    private java.sql.Timestamp endtime;

    @ManyToOne
    @JoinColumn(name = "DAYID", referencedColumnName = "DAYID")
    private Day day;

    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private User user;

    public Schedule(ScheduleDao scheduleDao)
    {
        this.scheduleid = scheduleDao.getScheduleid();
        this.starttime = Timestamp.from(scheduleDao.getStarttime());
        this.endtime = Timestamp.from(scheduleDao.getEndtime());
        this.day = new Day(scheduleDao.getDay());
        this.user = new User(scheduleDao.getUser());
    }
}
