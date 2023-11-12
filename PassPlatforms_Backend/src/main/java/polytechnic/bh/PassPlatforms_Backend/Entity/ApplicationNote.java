package polytechnic.bh.PassPlatforms_Backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_applicationnote")
public class ApplicationNote {

  @Id
  private int noteid;
  private Object datetime;
  private String notebody;

  @ManyToOne
  @JoinColumn(name = "APPLICATIONID", referencedColumnName = "APPLICATIONID")
  private Application application;

  @ManyToOne
  @JoinColumn(name = "USERID", referencedColumnName = "USERID")
  private User user;

}
