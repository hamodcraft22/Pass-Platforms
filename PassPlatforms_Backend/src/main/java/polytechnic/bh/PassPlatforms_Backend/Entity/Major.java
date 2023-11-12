package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_major")
public class Major {

  @Id
  private String majorid;
  private String majorname;
  private String majordesc;

  @ManyToOne
  @JoinColumn(name = "SCHOOLID", referencedColumnName = "SCHOOLID")
  private School school;

}
