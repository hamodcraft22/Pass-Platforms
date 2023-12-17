package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.DayDao;

import java.util.Calendar;

import static polytechnic.bh.PassPlatforms_Backend.Constant.DayConstant.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_day")
public class Day
{

    @Id
    private char dayid;
    private String dayname;

    public Day(DayDao dayDao)
    {
        this.dayid = dayDao.getDayid();
        this.dayname = dayDao.getDayname();
    }

    public int getDayNum()
    {
        switch (this.dayid)
        {
            case DAY_SUNDAY ->
            {
                return Calendar.SUNDAY;
            }
            case DAY_MONDAY ->
            {
                return Calendar.MONDAY;
            }
            case DAY_TUESDAY ->
            {
                return Calendar.TUESDAY;
            }
            case DAY_WEDNESDAY ->
            {
                return Calendar.WEDNESDAY;
            }
            case DAY_THURSDAY ->
            {
                return Calendar.THURSDAY;
            }
            case DAY_FRIDAY ->
            {
                return Calendar.FRIDAY;
            }
            case DAY_SATURDAY ->
            {
                return Calendar.SATURDAY;
            }
            default ->
            {
                return 0;
            }
        }
    }
}
