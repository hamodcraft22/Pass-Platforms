package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.CourseDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.OfferedCourseDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.CourseServ;
import polytechnic.bh.PassPlatforms_Backend.Service.OfferedCourseServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.ROLE_LEADER;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/offeredcourse")
public class OfferedCourseCont
{

    @Autowired
    private OfferedCourseServ offeredCourseServ;

    @Autowired
    private CourseServ courseServ;

    @Autowired
    private UserServ userServ;

    // get all offered courses - tested / not needed
    @GetMapping("")
    public ResponseEntity<GenericDto<List<OfferedCourseDao>>> getAllOfferedCourses(
            @RequestHeader(value = "Authorization") String requestKey
    )
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            List<OfferedCourseDao> offeredCourses = offeredCourseServ.getAllOfferedCourses();

            if (offeredCourses != null && !offeredCourses.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, offeredCourses, null, null), HttpStatus.OK);
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

    // get courses offered by a leader - tested
    @GetMapping("/leader/{leaderID}")
    public ResponseEntity<GenericDto<List<OfferedCourseDao>>> getLeaderOfferedCourses(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("leaderID") String leaderID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            List<OfferedCourseDao> offeredCourse = offeredCourseServ.getLeaderOfferedCourses(leaderID);

            if (offeredCourse != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, offeredCourse, null, null), HttpStatus.OK);
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

    // get leaders who are teaching the course - tested
    @GetMapping("/course/{courseID}")
    public ResponseEntity<GenericDto<List<OfferedCourseDao>>> getOfferedCourseLeaders(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("courseID") String courseID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            List<OfferedCourseDao> offeredCourse = offeredCourseServ.getOfferedCourseLeaders(courseID);

            if (offeredCourse != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, offeredCourse, null, null), HttpStatus.OK);
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

    // get offered course details - not needed
    @GetMapping("/{offerID}")
    public ResponseEntity<GenericDto<OfferedCourseDao>> getOfferedCourseDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("offerID") int offerID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            OfferedCourseDao offeredCourse = offeredCourseServ.getOfferedCourseDetails(offerID);

            if (offeredCourse != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, offeredCourse, null, null), HttpStatus.OK);
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

    // create offered course - tested
    @PostMapping("")
    public ResponseEntity<GenericDto<OfferedCourseDao>> createOfferedCourse(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody OfferedCourseDao offeredCourseDao)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_LEADER)
            {
                CourseDao course = courseServ.getCourseDetails(offeredCourseDao.getCourse().getCourseid());

                if (course != null)
                {
                    OfferedCourseDao createdOfferedCourse = offeredCourseServ.createOfferedCourse(
                            offeredCourseDao.getLeader().getUserid(),
                            offeredCourseDao.getCourse().getCourseid()
                    );

                    return new ResponseEntity<>(new GenericDto<>(null, createdOfferedCourse, null, null), HttpStatus.CREATED);
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

    // create offered course - tested | added
    @PostMapping("/multi")
    public ResponseEntity<GenericDto<List<OfferedCourseDao>>> createMultiOfferedCourse(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody List<OfferedCourseDao> offeredCourseDao)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_LEADER)
            {
                return new ResponseEntity<>(new GenericDto<>(null, offeredCourseServ.createMultiOfferedCourse(offeredCourseDao), null, null), HttpStatus.CREATED);
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

    // delete offered course - tested | added
    @DeleteMapping("/{offerID}")
    public ResponseEntity<GenericDto<Void>> deleteOfferedCourse(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("offerID") int offerID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_LEADER)
            {
                // check if the user has access
                OfferedCourseDao retrivedCourseOffer = offeredCourseServ.getOfferedCourseDetails(offerID);

                if (Objects.equals(retrivedCourseOffer.getLeader().getUserid(), userID))
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
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }
}

