package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Slot;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotDto {
    private int slotid;
    private Instant starttime;
    private Instant endtime;
    private String note;
    private boolean isrevision;
    private boolean isonline;
    private DayDto day;
    private UserDto leader;

    public SlotDto(Slot slot) {
        this.slotid = slot.getSlotid();
        this.starttime = slot.getStarttime().toInstant();
        this.endtime = slot.getEndtime().toInstant();
        this.note = slot.getNote();
        this.isrevision = slot.isIsrevision();
        this.isonline = slot.isIsonline();
        this.day = new DayDto(slot.getDay());
        this.leader = new UserDto(slot.getLeader());
    }
}
