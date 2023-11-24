package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.OfferedCourseDao;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_offeredcourse")
public class OfferedCourse
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pp_offer_SEQ")
    @SequenceGenerator(name = "pp_offer_SEQ", sequenceName = "pp_offer_SEQ", allocationSize = 1)
    private int offerid;

    @ManyToOne
    @JoinColumn(name = "LEADERID", referencedColumnName = "USERID")
    private User leader;

    @ManyToOne
    @JoinColumn(name = "COURSEID", referencedColumnName = "COURSEID")
    private Course course;

    public OfferedCourse(OfferedCourseDao offeredCourseDao)
    {
        this.offerid = offeredCourseDao.getOfferid();
        this.leader = new User(offeredCourseDao.getLeader());
        this.course = new Course(offeredCourseDao.getCourse());
    }
}
