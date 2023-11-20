package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.AuditDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.AuditServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.ADMIN_KEY;
import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.MANAGER_KEY;

@RestController
@RequestMapping("/api/audit")
public class AuditCont<T>
{
    @Autowired
    AuditServ auditServ;

    // get all audits
    @GetMapping("")
    public ResponseEntity<GenericDto<T>> getAllAudits(
            @RequestHeader(value = "Authorization", required = false) String requestKey)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            List<AuditDao> audits = auditServ.getAllAudits();

            if (audits != null && !audits.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, (T) audits, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // get audit details
    @GetMapping("/{auditID}")
    public ResponseEntity<GenericDto<T>> getAuditDetails(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @PathVariable("auditID") int auditID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            AuditDao audit = auditServ.getAuditDetails(auditID);

            if (audit != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, (T) audit, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // delete audit
    @DeleteMapping("/{auditID}")
    public ResponseEntity<GenericDto<T>> deleteAudit(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @PathVariable("auditID") int auditID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            if (auditServ.deleteAudit(auditID))
            {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
