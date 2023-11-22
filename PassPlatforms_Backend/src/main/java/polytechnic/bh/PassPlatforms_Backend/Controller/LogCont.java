package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.LogDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.LogServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.ADMIN_KEY;

@RestController
@RequestMapping("/api/log")
public class LogCont<T>
{
    @Autowired
    LogServ logServ;

    @GetMapping("")
    public ResponseEntity<GenericDto<List<LogDao>>> getAllLogs(
            @RequestHeader(value = "Authorization", required = false) String requestKey)
    {
        if (Objects.equals(requestKey, ADMIN_KEY))
        {
            List<LogDao> logs = logServ.getAllLogs();

            if (logs != null && !logs.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, logs, null), HttpStatus.OK);
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

    @GetMapping("/{logID}")
    public ResponseEntity<GenericDto<LogDao>> getLogDetails(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @PathVariable("logID") int logID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY))
        {
            LogDao log = logServ.getLogDetails(logID);

            if (log != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, log, null), HttpStatus.OK);
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

    @DeleteMapping("/{logID}")
    public ResponseEntity<GenericDto<T>> deleteLog(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @PathVariable("logID") int logID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY))
        {
            if (logServ.deleteLog(logID))
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
