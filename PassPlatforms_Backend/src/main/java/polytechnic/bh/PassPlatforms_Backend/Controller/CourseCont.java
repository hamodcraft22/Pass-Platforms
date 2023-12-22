package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.CourseDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.CourseServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.List;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.ROLE_ADMIN;
import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.ROLE_MANAGER;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/course")
public class CourseCont
{

    @Autowired
    private CourseServ courseServ;

    @Autowired
    private UserServ userServ;

    // Any person can use these
    @GetMapping("")
    public ResponseEntity<GenericDto<List<CourseDao>>> getAllCourses(
            @RequestHeader(value = "Authorization") String requestKey
    )
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            List<CourseDao> courses = courseServ.getAllCourses();

            if (courses != null && !courses.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, courses, null, null), HttpStatus.OK);
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

    // get course details
    @GetMapping("/{courseID}")
    public ResponseEntity<GenericDto<CourseDao>> getCourseDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("courseID") String courseID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            CourseDao course = courseServ.getCourseDetails(courseID);

            if (course != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, course, null, null), HttpStatus.OK);
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

    @GetMapping("/leader/{leaderID}")
    public ResponseEntity<GenericDto<List<CourseDao>>> getLeaderCourseDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("leaderID") String leaderID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            List<CourseDao> course = courseServ.getLeaderPossibleCourses(leaderID);

            if (course != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, course, null, null), HttpStatus.OK);
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

    // create course
    @PostMapping("")
    public ResponseEntity<GenericDto<CourseDao>> createCourse(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody CourseDao courseDao)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
            {
                CourseDao createdCourse = courseServ.createCourse(
                        courseDao.getCourseid(),
                        courseDao.getCoursename(),
                        courseDao.getSchool().getSchoolid()
                );

                return new ResponseEntity<>(new GenericDto<>(null, createdCourse, null, null), HttpStatus.CREATED);
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

    // multi create course
    @PostMapping("/multi/")
    public ResponseEntity<GenericDto<List<CourseDao>>> createMultiCourse(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody List<CourseDao> coursesDao)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
            {
                List<CourseDao> createdCourses = courseServ.createMultiCourse(coursesDao);

                return new ResponseEntity<>(new GenericDto<>(null, createdCourses, null, null), HttpStatus.CREATED);
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

    // edit course
    @PutMapping("")
    public ResponseEntity<GenericDto<CourseDao>> editCourse(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody CourseDao courseDao)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
            {
                CourseDao editedCourse = courseServ.editCourse(courseDao);

                if (editedCourse != null)
                {
                    return new ResponseEntity<>(new GenericDto<>(null, editedCourse, null, null), HttpStatus.OK);
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

    // delete course
    @DeleteMapping("/{courseID}")
    public ResponseEntity<GenericDto<Void>> deleteCourse(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("courseID") String courseID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
            {
                if (courseServ.deleteCourse(courseID))
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

