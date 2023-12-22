package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.CourseDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.TranscriptDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Course;
import polytechnic.bh.PassPlatforms_Backend.Entity.Transcript;
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

    @Autowired
    private TranscriptServ transcriptServ;

    public List<CourseDao> getAllCourses()
    {
        List<CourseDao> courses = new ArrayList<>();

        for (Course retrievedCourse : courseRepo.findAll())
        {
            courses.add(new CourseDao(retrievedCourse));
        }

        return courses;
    }

    public List<CourseDao> getRevCourses()
    {
        List<CourseDao> courses = new ArrayList<>();

        for (Course retrievedCourse : courseRepo.findRevCourses())
        {
            courses.add(new CourseDao(retrievedCourse));
        }

        return courses;
    }

    public List<CourseDao> getAvlbCourses()
    {
        List<CourseDao> courses = new ArrayList<>();

        for (Course retrievedCourse : courseRepo.findAvlbCourses())
        {
            courses.add(new CourseDao(retrievedCourse));
        }

        return courses;
    }

    public List<CourseDao> getLeaderPossibleCourses(String leaderID)
    {
        List<String> Grades = List.of("A+","A","A-","B+");

        List<CourseDao> leaderCourses = new ArrayList<>();

        // get all user trans courses
        List<TranscriptDao> leaderTrans = transcriptServ.getLeaderTranscripts(leaderID);

        List<String> courseIDs = new ArrayList<>();

        for (TranscriptDao transcript : leaderTrans)
        {
            if (Grades.contains(transcript.getGrade()))
            {
                courseIDs.add(transcript.getCourseid());
            }
        }

        // get all courses where course code in trans courses
        for (Course course : courseRepo.findLeaderPosbCourses(courseIDs))
        {
            leaderCourses.add(new CourseDao(course.getCourseid(), course.getCoursename(), null));
        }

        return leaderCourses;
    }

    public CourseDao getCourseDetails(String courseID)
    {
        Optional<Course> retrievedCourse = courseRepo.findById(courseID);

        return retrievedCourse.map(CourseDao::new).orElse(null);
    }

    public CourseDao createCourse(String courseID, String courseName, String schoolID)
    {
        Course newCourse = new Course();

        newCourse.setCourseid(courseID);
        newCourse.setCoursename(courseName);
        newCourse.setSchool(schoolRepo.getReferenceById(schoolID));

        return new CourseDao(courseRepo.save(newCourse));
    }

    public List<CourseDao> createMultiCourse(List<CourseDao> courses)
    {
        List<CourseDao> addedCourses = new ArrayList<>();

        for (CourseDao course : courses)
        {
            Course newCourse = new Course(course.getCourseid(), course.getCoursename(), schoolRepo.getReferenceById(course.getSchool().getSchoolid()));

            addedCourses.add(new CourseDao(courseRepo.save(newCourse)));
        }


        return addedCourses;
    }

    public CourseDao editCourse(CourseDao updatedCourse)
    {
        Optional<Course> retrievedCourse = courseRepo.findById(updatedCourse.getCourseid());

        if (retrievedCourse.isPresent())
        {
            retrievedCourse.get().setCoursename(updatedCourse.getCoursename());
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

