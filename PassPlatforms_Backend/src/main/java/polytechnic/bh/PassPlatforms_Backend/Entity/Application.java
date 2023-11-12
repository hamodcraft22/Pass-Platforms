package polytechnic.bh.PassPlatforms_Backend.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "pp_application")
public class Application {

  @Id
  private int applicationid;
  private Object datetime;
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
