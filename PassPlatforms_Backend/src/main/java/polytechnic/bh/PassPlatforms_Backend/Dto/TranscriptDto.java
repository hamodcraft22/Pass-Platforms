package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Transcript;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TranscriptDto {
    private int transid;
    private String grade;
    private UserDto student;
    private CourseDto course;

    public TranscriptDto(Transcript transcript) {
        this.transid = transcript.getTransid();
        this.grade = transcript.getGrade();
        this.student = new UserDto(transcript.getStudent());
        this.course = new CourseDto(transcript.getCourse());
    }
}
