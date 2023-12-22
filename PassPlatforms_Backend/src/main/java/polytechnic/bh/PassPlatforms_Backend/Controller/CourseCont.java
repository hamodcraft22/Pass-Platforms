package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.CourseDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.CourseServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.ADMIN_KEY;
import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.MANAGER_KEY;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/course")
public class CourseCont
{

    @Autowired
    private CourseServ courseServ;

    // Any person can use these
    @GetMapping("")
    public ResponseEntity<GenericDto<List<CourseDao>>> getAllCourses()
    {
        // Any person can use these

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

    // get course details
    @GetMapping("/{courseID}")
    public ResponseEntity<GenericDto<CourseDao>> getCourseDetails(@PathVariable("courseID") String courseID)
    {
        // Any person can use these

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

    @GetMapping("/leader/{leaderID}")
    public ResponseEntity<GenericDto<List<CourseDao>>> getLeaderCourseDetails(@PathVariable("leaderID") String leaderID)
    {
        // maybe validate against leader?

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

    // create course
    @PostMapping("")
    public ResponseEntity<GenericDto<CourseDao>> createCourse(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody CourseDao courseDao)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
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

    // multi create course
    @PostMapping("/multi/")
    public ResponseEntity<GenericDto<List<CourseDao>>> createMultiCourse(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody List<CourseDao> coursesDao)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            List<CourseDao> createdCourses = courseServ.createMultiCourse(coursesDao);

            return new ResponseEntity<>(new GenericDto<>(null, createdCourses, null, null), HttpStatus.CREATED);
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
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
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

    // delete course
    @DeleteMapping("/{courseID}")
    public ResponseEntity<GenericDto<Void>> deleteCourse(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("courseID") String courseID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
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
}

