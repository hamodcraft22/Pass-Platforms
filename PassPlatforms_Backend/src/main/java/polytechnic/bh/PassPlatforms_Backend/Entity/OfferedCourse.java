package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_offeredcourse")
public class OfferedCourse {

  @Id
  private int offerid;

  @ManyToOne
  @JoinColumn(name = "LEADERID", referencedColumnName = "USERID")
  private User leader;

  @ManyToOne
  @JoinColumn(name = "COURSEID", referencedColumnName = "COURSEID")
  private Course course;

}
