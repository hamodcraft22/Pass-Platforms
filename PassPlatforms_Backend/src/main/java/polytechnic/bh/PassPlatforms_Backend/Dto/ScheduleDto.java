package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Schedule;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleDto {
    private int scheduleid;
    private Instant starttime;
    private Instant endtime;
    private DayDto day;
    private UserDto user;

    public ScheduleDto(Schedule schedule) {
        this.scheduleid = schedule.getScheduleid();
        this.starttime = schedule.getStarttime().toInstant();
        this.endtime = schedule.getEndtime().toInstant();
        this.day = new DayDto(schedule.getDay());
        this.user = new UserDto(schedule.getUser());
    }
}
