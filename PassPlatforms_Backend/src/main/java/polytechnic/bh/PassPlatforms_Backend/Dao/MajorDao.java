package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Course;
import polytechnic.bh.PassPlatforms_Backend.Entity.Major;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MajorDao
{
    private String majorid;
    private String majorname;
    private String majordesc;
    private SchoolDao school;

    private List<CourseDao> courses;

    public MajorDao(Major major)
    {
        this.majorid = major.getMajorid();
        this.majorname = major.getMajorname();
        this.majordesc = major.getMajordesc();
        this.school = new SchoolDao(major.getSchool());

        //building custom list of objects while removing infinite recursion
        List<CourseDao> courses = new ArrayList<>();
        if (major.getCourses() != null && !major.getCourses().isEmpty())
        {
            for (Course course : major.getCourses())
            {
                courses.add(new CourseDao(
                        course.getCourseid(),
                        course.getCoursename(),
                        course.getCoursedesc(),
                        course.getSemaster(),
                        course.isAvailable(),
                        null
                ));
            }
        }
        this.courses = courses;
    }
}
