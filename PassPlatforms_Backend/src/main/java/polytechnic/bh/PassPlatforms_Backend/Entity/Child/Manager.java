package polytechnic.bh.PassPlatforms_Backend.Entity.Child;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
@Data
@Entity
@DiscriminatorValue("4")
@EqualsAndHashCode(callSuper = true)
public class Manager extends User {
}
