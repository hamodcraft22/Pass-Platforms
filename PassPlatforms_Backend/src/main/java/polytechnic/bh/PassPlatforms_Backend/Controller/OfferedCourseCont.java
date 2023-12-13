package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.CourseDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.OfferedCourseDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.CourseServ;
import polytechnic.bh.PassPlatforms_Backend.Service.OfferedCourseServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.LEADER_KEY;

@RestController
@RequestMapping("/api/offeredcourse")
public class OfferedCourseCont
{

    @Autowired
    private OfferedCourseServ offeredCourseServ;

    @Autowired
    private CourseServ courseServ;

    // get all offered courses
    @GetMapping("")
    public ResponseEntity<GenericDto<List<OfferedCourseDao>>> getAllOfferedCourses()
    {
        // any person can use these

        List<OfferedCourseDao> offeredCourses = offeredCourseServ.getAllOfferedCourses();

        if (offeredCourses != null && !offeredCourses.isEmpty())
        {
            return new ResponseEntity<>(new GenericDto<>(null, offeredCourses, null), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    // get courses offered by a leader
    @GetMapping("/leader/{leaderID}")
    public ResponseEntity<GenericDto<List<OfferedCourseDao>>> getLeaderOfferedCourses(
            @PathVariable("leaderID") String leaderID)
    {
        // Any person can use these

        List<OfferedCourseDao> offeredCourse = offeredCourseServ.getLeaderOfferedCourses(leaderID);

        if (offeredCourse != null)
        {
            return new ResponseEntity<>(new GenericDto<>(null, offeredCourse, null), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    // get leaders who are teaching the course
    @GetMapping("/course/{courseID}")
    public ResponseEntity<GenericDto<List<OfferedCourseDao>>> getOfferedCourseLeaders(
            @PathVariable("courseID") String courseID)
    {
        // Any person can use these

        List<OfferedCourseDao> offeredCourse = offeredCourseServ.getOfferedCourseLeaders(courseID);

        if (offeredCourse != null)
        {
            return new ResponseEntity<>(new GenericDto<>(null, offeredCourse, null), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    // get offered course details
    @GetMapping("/{offerID}")
    public ResponseEntity<GenericDto<OfferedCourseDao>> getOfferedCourseDetails(
            @PathVariable("offerID") int offerID)
    {
        // Any person can use these

        OfferedCourseDao offeredCourse = offeredCourseServ.getOfferedCourseDetails(offerID);

        if (offeredCourse != null)
        {
            return new ResponseEntity<>(new GenericDto<>(null, offeredCourse, null), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    // create offered course
    @PostMapping("")
    public ResponseEntity<GenericDto<OfferedCourseDao>> createOfferedCourse(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody OfferedCourseDao offeredCourseDao)
    {
        if (Objects.equals(requestKey, LEADER_KEY))
        {
            CourseDao course = courseServ.getCourseDetails(offeredCourseDao.getCourse().getCourseid());

            if (course != null && course.isAvailable())
            {
                OfferedCourseDao createdOfferedCourse = offeredCourseServ.createOfferedCourse(
                        offeredCourseDao.getLeader().getUserid(),
                        offeredCourseDao.getCourse().getCourseid()
                );

                return new ResponseEntity<>(new GenericDto<>(null, createdOfferedCourse, null), HttpStatus.CREATED);
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

    // delete offered course
    @DeleteMapping("/{offerID}")
    public ResponseEntity<GenericDto<Void>> deleteOfferedCourse(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("offerID") int offerID)
    {
        if (Objects.equals(requestKey, LEADER_KEY))
        {
            // check if the user has access
            OfferedCourseDao retrivedCourseOffer = offeredCourseServ.getOfferedCourseDetails(offerID);

            if (Objects.equals(retrivedCourseOffer.getLeader().getUserid(), requisterID))
            {
                if (offeredCourseServ.deleteOfferedCourse(offerID))
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
}

