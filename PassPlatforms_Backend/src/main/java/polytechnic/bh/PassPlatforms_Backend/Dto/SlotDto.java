package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Slot;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotDto {
    private int slotid;
    private java.sql.Timestamp starttime;
    private java.sql.Timestamp endtime;
    private String note;
    private String isrevision;
    private String isonline;
    private DayDto day;
    private UserDto leader;

    public SlotDto(Slot slot) {
        this.slotid = slot.getSlotid();
        this.starttime = slot.getStarttime();
        this.endtime = slot.getEndtime();
        this.note = slot.getNote();
        this.isrevision = slot.getIsrevision();
        this.isonline = slot.getIsonline();
        this.day = new DayDto(slot.getDay());
        this.leader = new UserDto(slot.getLeader());
    }
}
