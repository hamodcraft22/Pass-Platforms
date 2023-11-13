package polytechnic.bh.PassPlatforms_Backend.Dto;

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
public class ApplicationNoteDto {
    private int noteid;
    private Instant datetime;
    private String notebody;
    private ApplicationDto application;
    private UserDto user;

    public ApplicationNoteDto(ApplicationNote applicationNote) {
        this.noteid = applicationNote.getNoteid();
        this.datetime = applicationNote.getDatetime().toInstant();
        this.notebody = applicationNote.getNotebody();
        this.application = new ApplicationDto(applicationNote.getApplication().getApplicationid(),
                applicationNote.getApplication().getDatetime(),
                applicationNote.getApplication().getNote(),
                new ApplicationStatusDto(applicationNote.getApplication().getApplicationStatus()),
                new UserDto(applicationNote.getApplication().getUser()),
                null);
        this.user = new UserDto(applicationNote.getUser());
    }
}
