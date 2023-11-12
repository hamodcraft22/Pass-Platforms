package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_transcript")
public class Transcript {

  @Id
  private int transid;
  private String grade;

  @ManyToOne
  @JoinColumn(name = "STUDENTID", referencedColumnName = "USERID")
  private User student;

  @ManyToOne
  @JoinColumn(name = "COURSEID", referencedColumnName = "COURSEID")
  private Course course;

}
