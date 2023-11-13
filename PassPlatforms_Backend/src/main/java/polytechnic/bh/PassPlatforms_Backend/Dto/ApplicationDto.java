package polytechnic.bh.PassPlatforms_Backend.Dto;

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
public class ApplicationDto {
    private int applicationid;
    private Object datetime;
    private String note;
    private ApplicationStatusDto applicationStatus;
    private UserDto user;
    private List<ApplicationNoteDto> applicationNotes;

    public ApplicationDto(Application application) {
        this.applicationid = application.getApplicationid();
        this.datetime = application.getDatetime();
        this.note = application.getNote();

        //building custom dto objects for linked elements
        this.applicationStatus = new ApplicationStatusDto(application.getApplicationStatus());
        this.user = new UserDto(application.getUser());

        //building custom list of objects while removing infinite recursion
        List<ApplicationNoteDto> applicationNotes = new ArrayList<>();
        if(!application.getApplicationNotes().isEmpty())
        {
            for (ApplicationNote note : application.getApplicationNotes())
            {
                applicationNotes.add(new ApplicationNoteDto(note.getNoteid(),
                        note.getDatetime().toInstant(),
                        note.getNotebody(),
                        null,
                        new UserDto(note.getUser())));
            }
        }
        this.applicationNotes = applicationNotes;
    }
}
