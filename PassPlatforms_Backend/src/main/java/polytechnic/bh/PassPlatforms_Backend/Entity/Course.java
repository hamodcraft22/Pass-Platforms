package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import polytechnic.bh.PassPlatforms_Backend.Dao.CourseDao;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_course")
public class Course
{

    @Id
    private String courseid;
    private String coursename;
    private String coursedesc;
    private char semaster;
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "MAJORID", referencedColumnName = "MAJORID")
    private Major major;


    public Course(CourseDao courseDao)
    {
        this.courseid = courseDao.getCourseid();
        this.coursename = courseDao.getCoursename();
        this.coursedesc = courseDao.getCoursedesc();
        this.semaster = courseDao.getSemaster();
        this.available = courseDao.isAvailable();
        this.major = new Major(courseDao.getMajor());
    }
}
