package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Application;
import polytechnic.bh.PassPlatforms_Backend.Entity.ApplicationNote;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationDao {
    private int applicationid;
    private Object datetime;
    private String note;
    private ApplicationStatusDao applicationStatus;
    private UserDao user;
    private List<ApplicationNoteDao> applicationNotes;

    public ApplicationDao(Application application) {
        this.applicationid = application.getApplicationid();
        this.datetime = application.getDatetime();
        this.note = application.getNote();

        //building custom dto objects for linked elements
        this.applicationStatus = new ApplicationStatusDao(application.getApplicationStatus());
        this.user = new UserDao(application.getUser());

        //building custom list of objects while removing infinite recursion
        List<ApplicationNoteDao> applicationNotes = new ArrayList<>();
        if(!application.getApplicationNotes().isEmpty())
        {
            for (ApplicationNote note : application.getApplicationNotes())
            {
                applicationNotes.add(new ApplicationNoteDao(note.getNoteid(),
                        note.getDatetime().toInstant(),
                        note.getNotebody(),
                        null,
                        new UserDao(note.getUser())));
            }
        }
        this.applicationNotes = applicationNotes;
    }
}
