package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto {
    private int roleid;
    private String rolename;
    private String roledesc;

    public RoleDto(Role role) {
        this.roleid = role.getRoleid();
        this.rolename = role.getRolename();
        this.roledesc = role.getRoledesc();
    }
}
