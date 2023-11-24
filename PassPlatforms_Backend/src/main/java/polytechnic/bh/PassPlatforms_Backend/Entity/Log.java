package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.LogDao;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_log")
public class Log
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pp_log_SEQ")
    @SequenceGenerator(name = "pp_log_SEQ", sequenceName = "pp_log_SEQ", allocationSize = 1)
    private int logid;
    private String errormsg;
    private java.sql.Timestamp datetime;

    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private User user;

    public Log(LogDao logDao)
    {
        this.logid = logDao.getLogid();
        this.errormsg = logDao.getErrormsg();
        this.datetime = Timestamp.from(logDao.getDatetime());
        this.user = new User(logDao.getUser());
    }
}
