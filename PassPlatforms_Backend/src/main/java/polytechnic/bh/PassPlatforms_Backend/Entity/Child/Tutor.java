package polytechnic.bh.PassPlatforms_Backend.Entity.Child;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import polytechnic.bh.PassPlatforms_Backend.Entity.Recommendation;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;

import java.util.List;

@Data
@Entity
@DiscriminatorValue("3")
@EqualsAndHashCode(callSuper = true)
public class Tutor extends User
{

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "TUTORID", referencedColumnName = "USERID")
    private List<Recommendation> recommendations;
}
