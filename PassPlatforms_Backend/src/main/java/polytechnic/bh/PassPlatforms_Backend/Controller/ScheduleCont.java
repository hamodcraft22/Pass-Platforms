package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.ScheduleDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.ScheduleServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.*;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleCont
{

    @Autowired
    private ScheduleServ scheduleServ;

    // get all schedules
    @GetMapping("")
    public ResponseEntity<GenericDto<List<ScheduleDao>>> getAllSchedules(
            @RequestHeader(value = "Authorization", required = false) String requestKey)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            List<ScheduleDao> schedules = scheduleServ.getAllSchedules();

            if (schedules != null && !schedules.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, schedules, null), HttpStatus.OK);
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

    // get a user schedules
    @GetMapping("/user")
    public ResponseEntity<GenericDto<List<ScheduleDao>>> getUserSchedules(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @RequestHeader(value = "Requester", required = false) String requisterID)
    {
        if (Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY))
        {
            List<ScheduleDao> schedules = scheduleServ.getUserSchedules(requisterID);

            if (schedules != null && !schedules.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, schedules, null), HttpStatus.OK);
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

    // get schedule details
    @GetMapping("/{scheduleID}")
    public ResponseEntity<GenericDto<ScheduleDao>> getScheduleDetails(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @RequestHeader(value = "Requester", required = false) String requisterID,
            @PathVariable("scheduleID") int scheduleID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            ScheduleDao schedule = scheduleServ.getScheduleDetails(scheduleID);

            if (schedule != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, schedule, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else if (Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY))
        {
            ScheduleDao schedule = scheduleServ.getScheduleDetails(scheduleID);

            if (schedule != null)
            {
                if (Objects.equals(schedule.getUser().getUserid(), requisterID))
                {
                    return new ResponseEntity<>(new GenericDto<>(null, schedule, null), HttpStatus.OK);
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

    // create schedule
    @PostMapping("")
    public ResponseEntity<GenericDto<ScheduleDao>> createSchedule(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @RequestBody ScheduleDao scheduleDao)
    {
        if (Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY))
        {
            ScheduleDao createdSchedule = scheduleServ.createSchedule(
                    scheduleDao.getStarttime(),
                    scheduleDao.getEndtime(),
                    scheduleDao.getDay().getDayid(),
                    scheduleDao.getUser().getUserid()
            );

            return new ResponseEntity<>(new GenericDto<>(null, createdSchedule, null), HttpStatus.CREATED);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // edit schedule
    @PutMapping("")
    public ResponseEntity<GenericDto<ScheduleDao>> editSchedule(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @RequestHeader(value = "Requester", required = false) String requisterID,
            @RequestBody ScheduleDao scheduleDao)
    {
        if (Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY))
        {
            ScheduleDao editedSchedule = scheduleServ.getScheduleDetails(scheduleDao.getScheduleid());

            if (editedSchedule != null)
            {
                if (Objects.equals(scheduleDao.getUser().getUserid(), editedSchedule.getUser().getUserid()))
                {
                    return new ResponseEntity<>(new GenericDto<>(null, scheduleServ.editSchedule(scheduleDao), null), HttpStatus.OK);
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

    // delete schedule
    @DeleteMapping("/{scheduleID}")
    public ResponseEntity<GenericDto<Void>> deleteSchedule(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @RequestHeader(value = "Requester", required = false) String requisterID,
            @PathVariable("scheduleID") int scheduleID)
    {
        if (Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY))
        {
            ScheduleDao toDeleteSchedule = scheduleServ.getScheduleDetails(scheduleID);

            if (toDeleteSchedule != null)
            {
                if (Objects.equals(toDeleteSchedule.getUser().getUserid(), requisterID))
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
}

