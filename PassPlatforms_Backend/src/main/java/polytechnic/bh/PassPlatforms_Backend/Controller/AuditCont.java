package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.AuditDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.AuditServ;
import polytechnic.bh.PassPlatforms_Backend.Service.LogServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.List;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.ROLE_ADMIN;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/audit")
public class AuditCont
{
    @Autowired
    private AuditServ auditServ;

    @Autowired
    private UserServ userServ;

    @Autowired
    private LogServ logServ;

    // get all audits
    @GetMapping("")
    public ResponseEntity<GenericDto<List<AuditDao>>> getAllAudits(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_ADMIN)
                {
                    List<AuditDao> audits = auditServ.getAllAudits();

                    if (audits != null && !audits.isEmpty())
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, audits, null, null), HttpStatus.OK);
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
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // get audit details
    @GetMapping("/{auditID}")
    public ResponseEntity<GenericDto<AuditDao>> getAuditDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("auditID") int auditID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_ADMIN)
                {
                    AuditDao audit = auditServ.getAuditDetails(auditID);

                    if (audit != null)
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, audit, null, null), HttpStatus.OK);
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
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // delete audit
    @DeleteMapping("/{auditID}")
    public ResponseEntity<GenericDto<AuditDao>> deleteAudit(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("auditID") int auditID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_ADMIN)
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
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
