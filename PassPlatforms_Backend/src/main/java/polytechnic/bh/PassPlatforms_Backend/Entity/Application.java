package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationDao;

import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_application")
public class Application
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pp_application_SEQ")
    @SequenceGenerator(name = "pp_application_SEQ", sequenceName = "pp_application_SEQ", allocationSize = 1)
    private int applicationid;
    private java.sql.Timestamp datetime;
    private String note;

    @ManyToOne
    @JoinColumn(name = "APLCSTATUSID", referencedColumnName = "STATUSID")
    private ApplicationStatus applicationStatus;

    @OneToOne
    @JoinColumn(name = "studentid", referencedColumnName = "USERID")
    private User user;

    // custom (multi item) entities
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "application")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<ApplicationNote> applicationNotes;

    public Application(ApplicationDao application)
    {
        this.applicationid = application.getApplicationid();
        this.datetime = Timestamp.from(application.getDatetime());
        this.note = application.getNote();
        this.applicationStatus = new ApplicationStatus(application.getApplicationStatus());
        this.user = new User(application.getUser());
    }
}
