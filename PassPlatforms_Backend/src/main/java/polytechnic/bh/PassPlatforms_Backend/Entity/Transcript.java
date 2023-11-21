package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.TranscriptDao;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_transcript")
public class Transcript
{

    @Id
    private int transid;
    private String grade;

    @ManyToOne
    @JoinColumn(name = "STUDENTID", referencedColumnName = "USERID")
    private User student;

    @ManyToOne
    @JoinColumn(name = "COURSEID", referencedColumnName = "COURSEID")
    private Course course;

    public Transcript(TranscriptDao transcriptDao)
    {
        this.transid = transcriptDao.getTransid();
        this.grade = transcriptDao.getGrade();
        this.student = new User(transcriptDao.getStudent());
        this.course = new Course(transcriptDao.getCourse());
    }
}
