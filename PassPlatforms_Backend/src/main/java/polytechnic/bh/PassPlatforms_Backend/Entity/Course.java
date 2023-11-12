package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_course")
public class Course {

  @Id
  private String courseid;
  private String coursename;
  private String coursedesc;
  private String semaster;
  private String available;

  @ManyToOne
  @JoinColumn(name = "MAJORID", referencedColumnName = "MAJORID")
  private Major major;

}
