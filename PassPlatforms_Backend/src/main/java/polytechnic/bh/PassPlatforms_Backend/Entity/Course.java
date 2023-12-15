package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.CourseDao;

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

    @ManyToOne
    @JoinColumn(name = "SCHOOLID", referencedColumnName = "SCHOOLID")
    private School school;

    public Course(CourseDao courseDao)
    {
        this.courseid = courseDao.getCourseid();
        this.coursename = courseDao.getCoursename();
        this.school = new School(courseDao.getSchool());
    }
}
