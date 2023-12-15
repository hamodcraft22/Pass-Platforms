package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Course;
import polytechnic.bh.PassPlatforms_Backend.Entity.School;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchoolDao
{
    private String schoolid;
    private String schoolname;

    private List<CourseDao> courses;

    public SchoolDao(School school)
    {
        this.schoolid = school.getSchoolid();
        this.schoolname = school.getSchoolname();

        //building custom list of objects while removing infinite recursion
        List<CourseDao> courses = new ArrayList<>();
        if (school.getCourses() != null && !school.getCourses().isEmpty())
        {
            for (Course course : school.getCourses())
            {
                courses.add(new CourseDao(
                                course.getCourseid(),
                                course.getCoursename(),
                                null
                        )
                );
            }
        }
        this.courses = courses;
    }
}
