package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.OfferedCourse;

import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.getAzureAdName;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferedCourseDao
{
    private int offerid;
    private UserDao leader;
    private CourseDao course;

    public OfferedCourseDao(OfferedCourse offeredCourse)
    {
        this.offerid = offeredCourse.getOfferid();
        this.leader = new UserDao(offeredCourse.getLeader().getUserid(), new RoleDao(offeredCourse.getLeader().getRole()), getAzureAdName(offeredCourse.getLeader().getUserid()), null);
        this.course = new CourseDao(offeredCourse.getCourse().getCourseid(), offeredCourse.getCourse().getCoursename(), null);
    }
}
