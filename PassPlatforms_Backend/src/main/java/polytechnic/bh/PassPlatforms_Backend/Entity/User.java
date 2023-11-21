package polytechnic.bh.PassPlatforms_Backend.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ROLEID", discriminatorType = DiscriminatorType.INTEGER)
@Table(name = "pp_user")
public class User
{

    @Id
    private String userid;

    @ManyToOne
    @JoinColumn(name = "roleid", referencedColumnName = "roleid", insertable = false, updatable = false)
    private Role role;

    public User(UserDao userDao)
    {
        this.userid = userDao.getUserid();
        this.role = new Role(userDao.getRole());
    }
}
