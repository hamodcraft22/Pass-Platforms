package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.DayDao;

import java.util.Calendar;

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
            case 'u' ->
            {
                return Calendar.SUNDAY;
            }
            case 'm' ->
            {
                return Calendar.MONDAY;
            }
            case 't' ->
            {
                return Calendar.TUESDAY;
            }
            case 'w' ->
            {
                return Calendar.WEDNESDAY;
            }
            case 'r' ->
            {
                return Calendar.THURSDAY;
            }
            case 'f' ->
            {
                return Calendar.FRIDAY;
            }
            case 's' ->
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
