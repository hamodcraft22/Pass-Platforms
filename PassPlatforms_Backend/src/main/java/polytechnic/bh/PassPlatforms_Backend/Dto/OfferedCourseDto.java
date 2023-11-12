package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.OfferedCourse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferedCourseDto {
    private int offerid;
    private UserDto leader;
    private CourseDto course;

    public OfferedCourseDto(OfferedCourse offeredCourse) {
        this.offerid = offeredCourse.getOfferid();
        this.leader = new UserDto(offeredCourse.getLeader());
        this.course = new CourseDto(offeredCourse.getCourse());
    }
}
