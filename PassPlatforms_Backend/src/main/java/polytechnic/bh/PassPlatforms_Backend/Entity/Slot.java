package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.SlotDao;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_slot")
public class Slot
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pp_slot_SEQ")
    @SequenceGenerator(name = "pp_slot_SEQ", sequenceName = "pp_slot_SEQ", allocationSize = 1)
    private int slotid;
    private java.sql.Timestamp starttime;
    private java.sql.Timestamp endtime;
    private String note;

    @ManyToOne
    @JoinColumn(name = "typeid", referencedColumnName = "typeid")
    private SlotType slotType;

    @ManyToOne
    @JoinColumn(name = "DAYID", referencedColumnName = "DAYID")
    private Day day;

    @ManyToOne
    @JoinColumn(name = "LEADERID", referencedColumnName = "USERID")
    private User leader;

    public Slot(SlotDao slotDao)
    {
        this.slotid = slotDao.getSlotid();
        this.starttime = Timestamp.from(slotDao.getStarttime());
        this.endtime = Timestamp.from(slotDao.getEndtime());
        this.note = slotDao.getNote();
        this.slotType = new SlotType(slotDao.getSlotType());
        this.day = new Day(slotDao.getDay());
        this.leader = new User(slotDao.getLeader());
    }
}
