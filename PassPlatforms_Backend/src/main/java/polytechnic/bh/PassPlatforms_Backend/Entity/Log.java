package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_log")
public class Log {

  @Id
  private int logid;
  private String errormsg;
  private java.sql.Date datetime;

  @ManyToOne
  @JoinColumn(name = "USERID", referencedColumnName = "USERID")
  private User user;

}
