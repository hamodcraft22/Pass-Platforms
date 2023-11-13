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
  private char semaster;
  private boolean available;

  @ManyToOne
  @JoinColumn(name = "MAJORID", referencedColumnName = "MAJORID")
  private Major major;

}
