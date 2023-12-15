package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Slot;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotDao
{
    private int slotid;
    private Instant starttime;
    private Instant endtime;
    private String note;
    private SlotTypeDao slotType;
    private DayDao day;
    private UserDao leader;

    public SlotDao(Slot slot)
    {
        this.slotid = slot.getSlotid();
        this.starttime = slot.getStarttime().toInstant();
        this.endtime = slot.getEndtime().toInstant();
        this.note = slot.getNote();
        this.slotType = new SlotTypeDao(slot.getSlotType());
        this.day = new DayDao(slot.getDay());
        this.leader = new UserDao(slot.getLeader().getUserid(), new RoleDao(slot.getLeader().getRole()), null);
    }
}
