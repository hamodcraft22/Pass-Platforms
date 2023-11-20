package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.RoleDao;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_role")
public class Role
{

    @Id
    private int roleid;
    private String rolename;
    private String roledesc;

    public Role(RoleDao roleDao)
    {
        this.roleid = roleDao.getRoleid();
        this.rolename = roleDao.getRolename();
        this.roledesc = roleDao.getRoledesc();
    }
}
