package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.CourseDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Course;
import polytechnic.bh.PassPlatforms_Backend.Repository.CourseRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.SchoolRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServ
{

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private SchoolRepo schoolRepo;

    public List<CourseDao> getAllCourses()
    {
        List<CourseDao> courses = new ArrayList<>();

        for (Course retrievedCourse : courseRepo.findAll())
        {
            courses.add(new CourseDao(retrievedCourse));
        }

        return courses;
    }

    public CourseDao getCourseDetails(String courseID)
    {
        Optional<Course> retrievedCourse = courseRepo.findById(courseID);

        return retrievedCourse.map(CourseDao::new).orElse(null);
    }

    public CourseDao createCourse(String courseName, String courseDesc, char semester, boolean available, String schoolID)
    {
        Course newCourse = new Course();

        newCourse.setCoursename(courseName);
        newCourse.setCoursedesc(courseDesc);
        newCourse.setSemaster(semester);
        newCourse.setAvailable(available);
        newCourse.setSchool(schoolRepo.getReferenceById(schoolID));

        return new CourseDao(courseRepo.save(newCourse));
    }

    public CourseDao editCourse(CourseDao updatedCourse)
    {
        Optional<Course> retrievedCourse = courseRepo.findById(updatedCourse.getCourseid());

        if (retrievedCourse.isPresent())
        {
            retrievedCourse.get().setCoursename(updatedCourse.getCoursename());
            retrievedCourse.get().setCoursedesc(updatedCourse.getCoursedesc());
            retrievedCourse.get().setSemaster(updatedCourse.getSemaster());
            retrievedCourse.get().setAvailable(updatedCourse.isAvailable());
            retrievedCourse.get().setSchool(schoolRepo.getReferenceById(updatedCourse.getSchool().getSchoolid()));

            return new CourseDao(courseRepo.save(retrievedCourse.get()));
        }
        else
        {
            return null;
        }
    }

    public boolean deleteCourse(String courseID)
    {
        courseRepo.deleteById(courseID);
        return true;
    }
}

