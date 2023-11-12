package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_school")
public class School {

  @Id
  private String schoolid;
  private String schoolname;
  private String schooldesc;

}
