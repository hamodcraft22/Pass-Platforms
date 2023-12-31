package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Statistic;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticDao
{
    private int statID;
    private Date statdate;
    private int studentno;
    private int leaderno;
    private int bkdsessionsno;
    private int hoursno;

    private int ict;
    private int business;
    private int engineering;
    private int webmedia;
    private int visualdesign;
    private int logistics;
    private int other;

    public StatisticDao(Statistic statistic)
    {
        this.statID = statistic.getStatID();
        this.statdate = statistic.getStatdate();
        this.studentno = statistic.getStudentno();
        this.leaderno = statistic.getLeaderno();
        this.bkdsessionsno = statistic.getBkdsessionsno();
        this.hoursno = statistic.getHoursno();

        this.ict = statistic.getIct();
        this.business = statistic.getBusiness();
        this.engineering = statistic.getEngineering();
        this.webmedia = statistic.getWebmedia();
        this.visualdesign = statistic.getVisualdesign();
        this.logistics = statistic.getLogistics();
        this.other = statistic.getOther();
    }
}
