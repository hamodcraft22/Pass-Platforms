package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_schedule")
public class Schedule {

  @Id
  private int scheduleid;
  private java.sql.Timestamp starttime;
  private java.sql.Timestamp endtime;

  @ManyToOne
  @JoinColumn(name = "DAYID", referencedColumnName = "DAYID")
  private Day day;

  @ManyToOne
  @JoinColumn(name = "USERID", referencedColumnName = "USERID")
  private User user;

}
