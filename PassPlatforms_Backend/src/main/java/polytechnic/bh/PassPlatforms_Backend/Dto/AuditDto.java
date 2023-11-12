package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Audit;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditDto {
    private int auditid;
    private String changetype;
    private String entityname;
    private java.sql.Date datetime;
    private String oldvalue;
    private String newvalue;
    private UserDto user;

    public AuditDto(Audit audit) {
        this.auditid = audit.getAuditid();
        this.changetype = audit.getChangetype();
        this.entityname = audit.getEntityname();
        this.datetime = audit.getDatetime();
        this.oldvalue = audit.getOldvalue();
        this.newvalue = audit.getNewvalue();
        this.user = new UserDto(audit.getUser());
    }
}
