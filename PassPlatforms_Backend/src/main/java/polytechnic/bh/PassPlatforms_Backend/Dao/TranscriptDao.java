package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Transcript;

import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.getAzureAdName;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TranscriptDao
{
    private int transid;
    private String grade;
    private UserDao student;

    private String courseid;

    public TranscriptDao(Transcript transcript)
    {
        this.transid = transcript.getTransid();
        this.grade = transcript.getGrade();
        this.student = new UserDao(transcript.getStudent().getUserid(), new RoleDao(transcript.getStudent().getRole()), getAzureAdName(transcript.getStudent().getUserid()), null);
        this.courseid = transcript.getCourseid();
    }
}
