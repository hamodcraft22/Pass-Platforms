package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.OfferedCourse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferedCourseDao {
    private int offerid;
    private UserDao leader;
    private CourseDao course;

    public OfferedCourseDao(OfferedCourse offeredCourse) {
        this.offerid = offeredCourse.getOfferid();
        this.leader = new UserDao(offeredCourse.getLeader());
        this.course = new CourseDao(offeredCourse.getCourse());
    }
}