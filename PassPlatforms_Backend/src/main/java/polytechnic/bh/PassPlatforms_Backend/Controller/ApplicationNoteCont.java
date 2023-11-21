package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationNoteDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.ApplicationNoteServ;
import polytechnic.bh.PassPlatforms_Backend.Service.ApplicationServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.*;

@RestController
@RequestMapping("/api/applicationNote")
public class ApplicationNoteCont<T>
{

    @Autowired
    ApplicationNoteServ applicationNoteServ;

    @Autowired
    ApplicationServ applicationServ;

    // get all notes - note used - by admin or manager
    @GetMapping("")
    public ResponseEntity<GenericDto<List<ApplicationNoteDao>>> getAllNotes(
            @RequestHeader(value = "Authorization", required = false) String requestKey)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            List<ApplicationNoteDao> retrivedNotes = applicationNoteServ.getAllAplicationNotes(false);

            if (retrivedNotes != null && !retrivedNotes.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, retrivedNotes, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    // get all notes for an application - not needed (returned from main application with details)
    @GetMapping("/{applicationID}")
    public ResponseEntity<GenericDto<List<ApplicationNoteDao>>> getApplicationNotes(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @RequestHeader(value = "Requester", required = false) String requisterID,
            @PathVariable("applicationID") int applicationID)
    {
        // if it is an admin or manager, return anyway
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY))
        {
            List<ApplicationNoteDao> retrivedNotes = applicationNoteServ.getAplicationNotes(applicationID);

            if (retrivedNotes != null && !retrivedNotes.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, retrivedNotes, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        //if it is a student, check if it is their application
        else if (Objects.equals(requestKey, STUDENT_KEY))
        {
            // retrieve application
            List<ApplicationNoteDao> retrivedNotes = applicationNoteServ.getAplicationNotes(applicationID);

            // check if empty
            if (retrivedNotes != null && !retrivedNotes.isEmpty())
            {
                // check if application is of student
                if (Objects.equals(retrivedNotes.get(0).getApplication().getUser().getUserid(), requisterID))
                {
                    return new ResponseEntity<>(new GenericDto<>(null, retrivedNotes, null), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        // if any other type, do not return anything
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // get note details - specific application note
    @GetMapping("/{noteID}")
    public ResponseEntity<GenericDto<ApplicationNoteDao>> getNoteDetails(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @RequestHeader(value = "Requester", required = false) String requisterID,
            @PathVariable("noteID") int noteID)
    {
        // if it is an admin or manager, return anyway
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY))
        {
            ApplicationNoteDao retrivedNote = applicationNoteServ.getNoteDetials(noteID);

            if (retrivedNote != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, retrivedNote, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        //if it is a student, check if it is their application
        else if (Objects.equals(requestKey, STUDENT_KEY))
        {
            // retrieve note
            ApplicationNoteDao retrivedNote = applicationNoteServ.getNoteDetials(noteID);

            // check if empty
            if (retrivedNote != null)
            {
                // check if application is of student
                if (Objects.equals(retrivedNote.getApplication().getUser().getUserid(), requisterID))
                {
                    return new ResponseEntity<>(new GenericDto<>(null, retrivedNote, null), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        // if any other type, do not return anything
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // create note
    @PostMapping("/{applicationID}")
    public ResponseEntity<GenericDto<T>> createApplicationNote(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @RequestHeader(value = "Requester", required = false) String requisterID,
            @PathVariable("applicationID") int applicationID,
            @RequestBody String noteBody)
    {
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY))
        {
            // get application - verify it exists
            if (applicationServ.checkApplication(applicationID))
            {
                // add a note
                if (applicationNoteServ.creatApplicationNote(applicationID, noteBody, requisterID) != null)
                {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }
        else if (Objects.equals(requestKey, STUDENT_KEY))
        {
            ApplicationDao retrivedApplication = applicationServ.getApplicationDetailsByID(applicationID);

            if (retrivedApplication != null)
            {
                if (retrivedApplication.getUser().getUserid().equals(requisterID))
                {
                    // add a note
                    if (applicationNoteServ.creatApplicationNote(applicationID, noteBody, requisterID) != null)
                    {
                        return new ResponseEntity<>(null, HttpStatus.OK);
                    }
                    else
                    {
                        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                    }
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // edit note - not available

    // delete note
    @DeleteMapping("/{noteID}")
    public ResponseEntity<GenericDto<T>> deleteNote(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @RequestHeader(value = "Requester", required = false) String requisterID,
            @PathVariable("noteID") int noteID
    )
    {
        // only managers and admin are able to fully delete from the db
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY))
        {
            if (applicationNoteServ.deleteNote(noteID))
            {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        else if (Objects.equals(requestKey, STUDENT_KEY))
        {
            // retrieve note
            ApplicationNoteDao retrivedNote = applicationNoteServ.getNoteDetials(noteID);

            // check if empty
            if (retrivedNote != null)
            {
                // check if application is of student
                if (Objects.equals(retrivedNote.getApplication().getUser().getUserid(), requisterID))
                {
                    if (applicationNoteServ.deleteNote(noteID))
                    {
                        return new ResponseEntity<>(null, HttpStatus.OK);
                    }
                    else
                    {
                        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                    }
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}