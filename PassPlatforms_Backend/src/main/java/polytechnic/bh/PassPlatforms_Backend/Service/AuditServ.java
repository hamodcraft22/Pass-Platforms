package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import polytechnic.bh.PassPlatforms_Backend.Dao.AuditDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Audit;
import polytechnic.bh.PassPlatforms_Backend.Repository.AuditRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuditServ
{
    @Autowired
    AuditRepo auditRepo;

    @Autowired
    UserRepo userRepo;

    // get all audits
    public List<AuditDao> getAllAudits()
    {
        List<AuditDao> audits = new ArrayList<>();

        for (Audit retrivedAudit : auditRepo.findAll())
        {
            audits.add(new AuditDao(
                    retrivedAudit.getAuditid(),
                    retrivedAudit.getChangetype(),
                    retrivedAudit.getEntityname(),
                    retrivedAudit.getDatetime().toInstant(),
                    null,
                    null,
                    new UserDao(retrivedAudit.getUser())
            ));
        }

        return audits;
    }

    // get audit details
    public AuditDao getAuditDetails(int auditID)
    {
        Optional<Audit> retrivedAudit = auditRepo.findById(auditID);

        return retrivedAudit.map(AuditDao::new).orElse(null);
    }

    // create audit - not an api method
    public AuditDao createAudit(char changeType, String entityName, Objects oldValue, Objects newValue, String userID)
    {
        Audit newAudit = new Audit();

        newAudit.setChangetype(changeType);
        newAudit.setEntityname(entityName);
        newAudit.setDatetime(Timestamp.from(Instant.now()));
        newAudit.setOldvalue(SerializationUtils.serialize(oldValue));
        newAudit.setOldvalue(SerializationUtils.serialize(newValue));
        newAudit.setUser(userRepo.getReferenceById(userID));

       return new AuditDao(auditRepo.save(newAudit));
    }

    // delete audit
    public boolean deleteAudit(int auditID)
    {
        auditRepo.deleteById(auditID);
        return true;
    }
}
