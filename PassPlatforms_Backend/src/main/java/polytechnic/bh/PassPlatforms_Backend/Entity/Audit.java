package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.AuditDao;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_audit")
public class Audit
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pp_audit_SEQ")
    @SequenceGenerator(name = "pp_audit_SEQ", sequenceName = "pp_audit_SEQ", allocationSize = 1)
    private int auditid;
    private char changetype;
    private String entityname;
    private java.sql.Timestamp datetime;
    @Lob
    private byte[] oldvalue;
    @Lob
    private byte[] newvalue;

    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private User user;

    public Audit(AuditDao auditDao)
    {
        this.auditid = auditDao.getAuditid();
        this.changetype = auditDao.getChangetype();
        this.entityname = auditDao.getEntityname();
        this.datetime = Timestamp.from(auditDao.getDatetime());
        this.oldvalue = auditDao.getOldvalue();
        this.newvalue = auditDao.getNewvalue();
        this.user = new User(auditDao.getUser());
    }
}
