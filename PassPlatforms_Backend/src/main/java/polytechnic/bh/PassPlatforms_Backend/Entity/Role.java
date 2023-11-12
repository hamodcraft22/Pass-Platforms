package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "pp_role")
public class Role {

  @Id
  private int roleid;
  private String rolename;
  private String roledesc;

}
