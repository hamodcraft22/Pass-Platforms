package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_studentmajor")
public class StudentMajor {

  @Id
  private int stumajorid;
  private String isminor;

  @ManyToOne
  @JoinColumn(name = "USERID", referencedColumnName = "USERID")
  private User user;

  @ManyToOne
  @JoinColumn(name = "MAJORID", referencedColumnName = "MAJORID")
  private Major major;

}
