package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationNoteDao;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_applicationnote")
public class ApplicationNote
{

    @Id
    private int noteid;
    private java.sql.Timestamp datetime;
    private String notebody;

    @ManyToOne
    @JoinColumn(name = "APPLICATIONID", referencedColumnName = "APPLICATIONID")
    private Application application;

    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private User user;

    public ApplicationNote(ApplicationNoteDao applicationNoteDao)
    {
        this.noteid = applicationNoteDao.getNoteid();
        this.datetime = Timestamp.from(applicationNoteDao.getDatetime());
        this.notebody = applicationNoteDao.getNotebody();
        this.application = new Application(applicationNoteDao.getApplication());
        this.user = new User(applicationNoteDao.getUser());
    }
}
