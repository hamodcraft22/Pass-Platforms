package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.StatisticDao;

import java.sql.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_statistic")
public class Statistic
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pp_stat_SEQ")
    @SequenceGenerator(name = "pp_stat_SEQ", sequenceName = "pp_stat_SEQ", allocationSize = 1)
    private int statID;

    private java.sql.Date statdate;
    private int studentno;
    private int leaderno;
    private int ofrdcoursesno;
    private int bkdsessionsno;
    private int hlpdno;
    private int hoursno;
    private int avgdayssnsno;
    private int avragepercent;

    private int ict;
    private int business;
    private int engineering;
    private int webmedia;
    private int visualdesign;
    private int logistics;
    private int other;

    public Statistic(StatisticDao statisticDao)
    {
        this.statID = statisticDao.getStatID();
        this.statdate = new Date(statisticDao.getStatdate().getTime());
        this.studentno = statisticDao.getStudentno();
        this.leaderno = statisticDao.getLeaderno();
        this.ofrdcoursesno = statisticDao.getOfrdcoursesno();
        this.bkdsessionsno = statisticDao.getBkdsessionsno();
        this.hlpdno = statisticDao.getHlpdno();
        this.hoursno = statisticDao.getHoursno();
        this.avgdayssnsno = statisticDao.getAvgdayssnsno();
        this.avragepercent = statisticDao.getAvragepercent();

        this.ict = statisticDao.getIct();
        this.business = statisticDao.getBusiness();
        this.engineering = statisticDao.getEngineering();
        this.webmedia = statisticDao.getWebmedia();
        this.visualdesign = statisticDao.getVisualdesign();
        this.logistics = statisticDao.getLogistics();
        this.other = statisticDao.getOther();
    }
}
