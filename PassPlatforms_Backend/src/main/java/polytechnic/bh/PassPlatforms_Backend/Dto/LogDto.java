package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Log;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogDto {
    private int logid;
    private String errormsg;
    private java.sql.Date datetime;
    private UserDto user;

    public LogDto(Log log) {
        this.logid = log.getLogid();
        this.errormsg = log.getErrormsg();
        this.datetime = log.getDatetime();
        this.user = new UserDto(log.getUser());
    }
}
