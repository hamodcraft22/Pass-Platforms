package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Course;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDao {
    private String courseid;
    private String coursename;
    private String coursedesc;
    private char semaster;
    private boolean available;
    private MajorDao major;

    public CourseDao(Course course) {
        this.courseid = course.getCourseid();
        this.coursename = course.getCoursename();
        this.coursedesc = course.getCoursedesc();
        this.semaster = course.getSemaster();
        this.available = course.isAvailable();
        this.major = new MajorDao(course.getMajor());
    }
}