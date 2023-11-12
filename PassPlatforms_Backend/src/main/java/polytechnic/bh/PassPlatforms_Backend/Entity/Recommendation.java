package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_recommendation")
public class Recommendation {

  @Id
  private int recid;
  private java.sql.Date datetime;
  private String note;

  @ManyToOne
  @JoinColumn(name = "STATUSID", referencedColumnName = "STATUSID")
  private RecStatus status;

  @ManyToOne
  @JoinColumn(name = "TUTORID", referencedColumnName = "USERID")
  private User tutor;

  @ManyToOne
  @JoinColumn(name = "STUDENTID", referencedColumnName = "USERID")
  private User student;

}
