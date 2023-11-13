package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "pp_application")
public class Application {

  @Id
  private int applicationid;
  private java.sql.Timestamp datetime;
  private String note;

  @ManyToOne
  @JoinColumn(name = "APLCSTATUSID", referencedColumnName = "STATUSID")
  private ApplicationStatus applicationStatus;

  @OneToOne
  @JoinColumn(name = "studentid", referencedColumnName = "USERID")
  private User user;

  // custom (multi item) entities
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "APPLICATIONID", referencedColumnName = "APPLICATIONID")
  private List<ApplicationNote> applicationNotes;

}
