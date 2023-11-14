package polytechnic.bh.PassPlatforms_Backend.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ROLEID", discriminatorType = DiscriminatorType.INTEGER)
@Table(name = "pp_user")
public class User {

  @Id
  private String userid;

  @ManyToOne
  @JoinColumn(name = "roleid", referencedColumnName = "roleid", insertable = false, updatable = false)
  private Role role;

}
