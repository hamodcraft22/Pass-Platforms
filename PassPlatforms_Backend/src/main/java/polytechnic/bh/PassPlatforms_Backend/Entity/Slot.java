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
    private int slotid;
    private java.sql.Timestamp starttime;
    private java.sql.Timestamp endtime;
    private String note;
    private boolean isrevision;
    private boolean isonline;

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
        this.isrevision = slotDao.isIsrevision();
        this.isonline = slotDao.isIsonline();
        this.day = new Day(slotDao.getDay());
        this.leader = new User(slotDao.getLeader());
    }
}
