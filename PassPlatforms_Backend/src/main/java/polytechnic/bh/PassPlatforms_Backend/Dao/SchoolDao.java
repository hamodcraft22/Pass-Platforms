package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Course;
import polytechnic.bh.PassPlatforms_Backend.Entity.Major;
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
    private String schooldesc;

    private List<MajorDao> majors;

    public SchoolDao(School school)
    {
        this.schoolid = school.getSchoolid();
        this.schoolname = school.getSchoolname();
        this.schooldesc = school.getSchooldesc();

        //building custom list of objects while removing infinite recursion
        List<MajorDao> majors = new ArrayList<>();
        if (school.getMajors() != null && !school.getMajors().isEmpty())
        {
            for (Major major : school.getMajors())
            {
                // inner list of list
                List<CourseDao> courses = new ArrayList<>();
                if (major.getCourses() != null && !major.getCourses().isEmpty())
                {
                    for (Course course : major.getCourses())
                    {
                        courses.add(new CourseDao(course));
                    }
                }

                majors.add(new MajorDao(
                        major.getMajorid(),
                        major.getMajorname(),
                        major.getMajordesc(),
                        null,
                        courses
                ));
            }
        }
        this.majors = majors;
    }
}
