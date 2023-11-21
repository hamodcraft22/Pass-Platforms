package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.LogDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Log;
import polytechnic.bh.PassPlatforms_Backend.Repository.LogRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LogServ
{
    @Autowired
    LogRepo logRepo;

    @Autowired
    UserRepo userRepo;

    // get all logs
    public List<LogDao> getAllLogs()
    {
        List<LogDao> logs = new ArrayList<>();

        for (Log retrivedLog : logRepo.findAll())
        {
            logs.add(new LogDao(
                    retrivedLog.getLogid(),
                    null,
                    retrivedLog.getDatetime().toInstant(),
                    new UserDao(retrivedLog.getUser())
            ));
        }

        return logs;
    }

    // gel logs details
    public LogDao getLogDetails(int logID)
    {
        Optional<Log> retrivedLog = logRepo.findById(logID);

        return retrivedLog.map(LogDao::new).orElse(null);
    }

    // create log - not api method
    public LogDao createLog(String errorMsg, String userID)
    {
        Log newLog = new Log();

        newLog.setErrormsg(errorMsg);
        newLog.setDatetime(Timestamp.from(Instant.now()));
        newLog.setUser(userRepo.getReferenceById(userID));

        return new LogDao(logRepo.save(newLog));
    }

    // delete log
    public boolean deleteLog(int logID)
    {
        logRepo.deleteById(logID);
        return true;
    }
}
