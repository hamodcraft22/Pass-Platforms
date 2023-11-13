package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_audit")
public class Audit {

  @Id
  private int auditid;
  private char changetype;
  private String entityname;
  private java.sql.Timestamp datetime;
  private String oldvalue;
  private String newvalue;

  @ManyToOne
  @JoinColumn(name = "USERID", referencedColumnName = "USERID")
  private User user;

}
