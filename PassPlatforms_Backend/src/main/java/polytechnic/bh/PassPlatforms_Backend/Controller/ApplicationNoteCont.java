package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationNoteDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.ApplicationNoteServ;
import polytechnic.bh.PassPlatforms_Backend.Service.ApplicationServ;
import polytechnic.bh.PassPlatforms_Backend.Service.LogServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.*;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/applicationNote")
public class ApplicationNoteCont
{

    @Autowired
    private ApplicationNoteServ applicationNoteServ;

    @Autowired
    private ApplicationServ applicationServ;

    @Autowired
    private UserServ userServ;

    @Autowired
    private LogServ logServ;

    // get all notes - note used - by admin or manager
    @GetMapping("")
    public ResponseEntity<GenericDto<List<ApplicationNoteDao>>> getAllNotes(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
                {
                    List<ApplicationNoteDao> retrivedNotes = applicationNoteServ.getAllAplicationNotes(false);

                    if (retrivedNotes != null && !retrivedNotes.isEmpty())
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, retrivedNotes, null, null), HttpStatus.OK);
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
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // get all notes for an application - not needed (returned from main application with details)
    @GetMapping("/{applicationID}")
    public ResponseEntity<GenericDto<List<ApplicationNoteDao>>> getApplicationNotes(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("applicationID") int applicationID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                // if it is an admin or manager, return anyway
                if (user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_ADMIN)
                {
                    List<ApplicationNoteDao> retrivedNotes = applicationNoteServ.getAplicationNotes(applicationID);

                    if (retrivedNotes != null && !retrivedNotes.isEmpty())
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, retrivedNotes, null, null), HttpStatus.OK);
                    }
                    else
                    {
                        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                    }
                }
                //if it is a student, check if it is their application
                else if (user.getRole().getRoleid() == ROLE_STUDENT)
                {
                    // retrieve application
                    List<ApplicationNoteDao> retrivedNotes = applicationNoteServ.getAplicationNotes(applicationID);

                    // check if empty
                    if (retrivedNotes != null && !retrivedNotes.isEmpty())
                    {
                        // check if application is of student
                        if (Objects.equals(retrivedNotes.get(0).getApplication().getUser().getUserid(), userID))
                        {
                            return new ResponseEntity<>(new GenericDto<>(null, retrivedNotes, null, null), HttpStatus.OK);
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
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // get note details - specific application note
    @GetMapping("/{noteID}")
    public ResponseEntity<GenericDto<ApplicationNoteDao>> getNoteDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("noteID") int noteID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                // if it is an admin or manager, return anyway
                if (user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_ADMIN)
                {
                    ApplicationNoteDao retrivedNote = applicationNoteServ.getNoteDetials(noteID);

                    if (retrivedNote != null)
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, retrivedNote, null, null), HttpStatus.OK);
                    }
                    else
                    {
                        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                    }
                }
                //if it is a student, check if it is their application
                else if (user.getRole().getRoleid() == ROLE_STUDENT)
                {
                    // retrieve note
                    ApplicationNoteDao retrivedNote = applicationNoteServ.getNoteDetials(noteID);

                    // check if empty
                    if (retrivedNote != null)
                    {
                        // check if application is of student
                        if (Objects.equals(retrivedNote.getApplication().getUser().getUserid(), userID))
                        {
                            return new ResponseEntity<>(new GenericDto<>(null, retrivedNote, null, null), HttpStatus.OK);
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
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // create note
    @PostMapping("/{applicationID}")
    public ResponseEntity<GenericDto<ApplicationNoteDao>> createApplicationNote(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("applicationID") int applicationID,
            @RequestBody String noteBody)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_ADMIN)
                {
                    // get application - verify it exists
                    if (applicationServ.checkApplication(applicationID))
                    {
                        // add a note
                        if (applicationNoteServ.creatApplicationNote(applicationID, noteBody, userID) != null)
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
                else if (user.getRole().getRoleid() == ROLE_STUDENT)
                {
                    ApplicationDao retrivedApplication = applicationServ.getApplicationDetailsByID(applicationID);

                    if (retrivedApplication != null)
                    {
                        if (retrivedApplication.getUser().getUserid().equals(userID))
                        {
                            // add a note
                            if (applicationNoteServ.creatApplicationNote(applicationID, noteBody, userID) != null)
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
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // delete note
    @DeleteMapping("/{noteID}")
    public ResponseEntity<GenericDto<ApplicationNoteDao>> deleteNote(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("noteID") int noteID
    )
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                // only managers and admin are able to fully delete from the db
                if (user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_ADMIN)
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
                else if (user.getRole().getRoleid() == ROLE_STUDENT)
                {
                    // retrieve note
                    ApplicationNoteDao retrivedNote = applicationNoteServ.getNoteDetials(noteID);

                    // check if empty
                    if (retrivedNote != null)
                    {
                        // check if application is of student
                        if (Objects.equals(retrivedNote.getApplication().getUser().getUserid(), userID))
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
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }

        }
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
