package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.ApplicationNote;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationNoteDao {
    private int noteid;
    private Instant datetime;
    private String notebody;
    private ApplicationDao application;
    private UserDao user;

    public ApplicationNoteDao(ApplicationNote applicationNote) {
        this.noteid = applicationNote.getNoteid();
        this.datetime = applicationNote.getDatetime().toInstant();
        this.notebody = applicationNote.getNotebody();
        this.application = new ApplicationDao(applicationNote.getApplication().getApplicationid(),
                applicationNote.getApplication().getDatetime(),
                applicationNote.getApplication().getNote(),
                new ApplicationStatusDao(applicationNote.getApplication().getApplicationStatus()),
                new UserDao(applicationNote.getApplication().getUser()),
                null);
        this.user = new UserDao(applicationNote.getUser());
    }
}