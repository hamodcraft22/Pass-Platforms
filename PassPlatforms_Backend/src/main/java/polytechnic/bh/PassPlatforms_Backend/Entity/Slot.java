package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_slot")
public class Slot {

  @Id
  private int slotid;
  private java.sql.Timestamp starttime;
  private java.sql.Timestamp endtime;
  private String note;
  private String isrevision;
  private String isonline;

  @ManyToOne
  @JoinColumn(name = "DAYID", referencedColumnName = "DAYID")
  private Day day;

  @ManyToOne
  @JoinColumn(name = "LEADERID", referencedColumnName = "USERID")
  private User leader;

}
