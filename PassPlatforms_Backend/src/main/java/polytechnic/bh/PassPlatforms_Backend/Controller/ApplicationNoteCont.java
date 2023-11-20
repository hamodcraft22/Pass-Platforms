package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationNoteDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Entity.ApplicationNote;
import polytechnic.bh.PassPlatforms_Backend.Repository.ApplicationNoteRepo;
import polytechnic.bh.PassPlatforms_Backend.Service.ApplicationNoteServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.ADMIN_KEY;
import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.MANAGER_KEY;

@RestController
@RequestMapping("/api/applicationNote")
public class ApplicationNoteCont<T> {

    @Autowired
    ApplicationNoteServ applicationNoteServ;

    // get all notes - note used - by admin or manager
    @GetMapping("")
    public ResponseEntity<GenericDto<T>> getAllNotes(
            @RequestHeader(value = "requestKey", required = false) String requestKey) {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY)) {
            List<ApplicationNoteDao> retrivedNotes = applicationNoteServ.getAllAplicationNotes(false);

            if (retrivedNotes != null && !retrivedNotes.isEmpty()) {
                return new ResponseEntity<>(new GenericDto<>(null, (T) retrivedNotes, null), HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    // get all notes for an application

    // get note details - specific application note

    // edit note

    // delete note
}