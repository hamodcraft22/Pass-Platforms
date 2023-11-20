package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDao
{
    private int roleid;
    private String rolename;
    private String roledesc;

    public RoleDao(Role role)
    {
        this.roleid = role.getRoleid();
        this.rolename = role.getRolename();
        this.roledesc = role.getRoledesc();
    }
}
