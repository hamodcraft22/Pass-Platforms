package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.ScheduleDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.LogServ;
import polytechnic.bh.PassPlatforms_Backend.Service.ScheduleServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.*;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/schedule")
public class ScheduleCont
{

    @Autowired
    private ScheduleServ scheduleServ;

    @Autowired
    private UserServ userServ;

    @Autowired
    private LogServ logServ;

    // get all schedules -- not really needed
    @GetMapping("")
    public ResponseEntity<GenericDto<List<ScheduleDao>>> getAllSchedules(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
                {
                    List<ScheduleDao> schedules = scheduleServ.getAllSchedules();

                    if (schedules != null && !schedules.isEmpty())
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, schedules, null, null), HttpStatus.OK);
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

    // get a user schedules -- added | tested
    @GetMapping("/student/{studentID}")
    public ResponseEntity<GenericDto<List<ScheduleDao>>> getUserSchedules(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable(value = "studentID") String studentID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {

                List<ScheduleDao> schedules = scheduleServ.getUserSchedules(studentID);

                if (schedules != null && !schedules.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, schedules, null, null), HttpStatus.OK);
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // get schedule details -- not needed
    @GetMapping("/{scheduleID}")
    public ResponseEntity<GenericDto<ScheduleDao>> getScheduleDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("scheduleID") int scheduleID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
                {
                    ScheduleDao schedule = scheduleServ.getScheduleDetails(scheduleID);

                    if (schedule != null)
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, schedule, null, null), HttpStatus.OK);
                    }
                    else
                    {
                        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                    }
                }
                else if (user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER)
                {
                    ScheduleDao schedule = scheduleServ.getScheduleDetails(scheduleID);

                    if (schedule != null)
                    {
                        if (Objects.equals(schedule.getUser().getUserid(), userID))
                        {
                            return new ResponseEntity<>(new GenericDto<>(null, schedule, null, null), HttpStatus.OK);
                        }
                        else
                        {
                            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                        }
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

    // create schedule -- added | tested
    @PostMapping("")
    public ResponseEntity<GenericDto<ScheduleDao>> createSchedule(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody ScheduleDao scheduleDao)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER)
                {
                    ScheduleDao createdSchedule = scheduleServ.createSchedule(
                            scheduleDao.getStarttime(),
                            scheduleDao.getEndtime(),
                            scheduleDao.getDay().getDayid(),
                            scheduleDao.getUser().getUserid()
                    );

                    return new ResponseEntity<>(new GenericDto<>(null, createdSchedule, null, null), HttpStatus.CREATED);
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

    // create multi schedule -- added | tested
    @PostMapping("/multi")
    public ResponseEntity<GenericDto<List<ScheduleDao>>> createMultiSchedule(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody List<ScheduleDao> scheduleDao)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER)
                {
                    List<ScheduleDao> createdSchedules = scheduleServ.createMultiSchedule(scheduleDao);

                    return new ResponseEntity<>(new GenericDto<>(null, createdSchedules, null, null), HttpStatus.CREATED);
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

    // edit schedule -- added | tested
    @PutMapping("")
    public ResponseEntity<GenericDto<ScheduleDao>> editSchedule(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody ScheduleDao scheduleDao)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER)
                {
                    ScheduleDao editedSchedule = scheduleServ.getScheduleDetails(scheduleDao.getScheduleid());

                    if (editedSchedule != null)
                    {
                        if (Objects.equals(editedSchedule.getUser().getUserid(), userID))
                        {
                            return new ResponseEntity<>(new GenericDto<>(null, scheduleServ.editSchedule(scheduleDao), null, null), HttpStatus.OK);
                        }
                        else
                        {
                            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                        }
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

    // delete schedule -- added | tested
    @DeleteMapping("/{scheduleID}")
    public ResponseEntity<GenericDto<Void>> deleteSchedule(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("scheduleID") int scheduleID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER)
                {
                    ScheduleDao toDeleteSchedule = scheduleServ.getScheduleDetails(scheduleID);

                    if (toDeleteSchedule != null)
                    {
                        if (Objects.equals(toDeleteSchedule.getUser().getUserid(), userID))
                        {
                            if (scheduleServ.deleteSchedule(scheduleID))
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

