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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pp_transcript_SEQ")
    @SequenceGenerator(name = "pp_transcript_SEQ", sequenceName = "pp_transcript_SEQ", allocationSize = 1)
    private int transid;
    private String grade;

    @ManyToOne
    @JoinColumn(name = "STUDENTID", referencedColumnName = "USERID")
    private User student;

    private String courseid;

    public Transcript(TranscriptDao transcriptDao)
    {
        this.transid = transcriptDao.getTransid();
        this.grade = transcriptDao.getGrade();
        this.student = new User(transcriptDao.getStudent());
        this.courseid = transcriptDao.getCourseid();
    }
}
