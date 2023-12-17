package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Log;

import java.time.Instant;

import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.getAzureAdName;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogDao
{
    private int logid;
    private String errormsg;
    private Instant datetime;
    private UserDao user;

    public LogDao(Log log)
    {
        this.logid = log.getLogid();
        this.errormsg = log.getErrormsg();
        this.datetime = log.getDatetime().toInstant();
        this.user = new UserDao(log.getUser().getUserid(), new RoleDao(log.getUser().getRole()), getAzureAdName(log.getUser().getUserid()), null);
    }
}
