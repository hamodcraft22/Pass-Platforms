package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Audit;

import java.time.Instant;

import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.getAzureAdName;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditDao
{
    private int auditid;
    private char changetype;
    private String entityname;
    private Instant datetime;
    private byte[] oldvalue;
    private byte[] newvalue;
    private UserDao user;

    public AuditDao(Audit audit)
    {
        this.auditid = audit.getAuditid();
        this.changetype = audit.getChangetype();
        this.entityname = audit.getEntityname();
        this.datetime = audit.getDatetime().toInstant();
        this.oldvalue = audit.getOldvalue();
        this.newvalue = audit.getNewvalue();
        this.user = new UserDao(audit.getUser().getUserid(), new RoleDao(audit.getUser().getRole()), getAzureAdName(audit.getUser().getUserid()), null);
    }
}
