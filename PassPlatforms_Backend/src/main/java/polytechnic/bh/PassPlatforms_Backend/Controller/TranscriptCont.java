package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.TranscriptDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.TranscriptServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.*;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/transcript")
public class TranscriptCont
{

    @Autowired
    private TranscriptServ transcriptServ;

    @Autowired
    private UserServ userServ;

    // get all transcripts
    @GetMapping("")
    public ResponseEntity<GenericDto<List<TranscriptDao>>> getAllTranscripts(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
            {
                List<TranscriptDao> transcripts = transcriptServ.getAllTranscripts();

                if (transcripts != null && !transcripts.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, transcripts, null, null), HttpStatus.OK);
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

    @GetMapping("/leader/{leaderID}")
    public ResponseEntity<GenericDto<List<TranscriptDao>>> getLeaderTranscripts(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("leaderID") String leaderID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            // if it is an admin or manager, return anyway
            if (user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_ADMIN)
            {
                List<TranscriptDao> transcripts = transcriptServ.getLeaderTranscripts(leaderID);

                if (transcripts != null && !transcripts.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, transcripts, null, null), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                }
            }
            //if it is a student, check if it is their application
            else if (user.getRole().getRoleid() == ROLE_STUDENT)
            {
                List<TranscriptDao> transcripts = transcriptServ.getLeaderTranscripts(leaderID);

                if (transcripts != null && !transcripts.isEmpty())
                {
                    if (Objects.equals(userID, leaderID))
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, transcripts, null, null), HttpStatus.OK);
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

    // get transcript details
    @GetMapping("/{transID}")
    public ResponseEntity<GenericDto<TranscriptDao>> getTranscriptDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("transID") int transID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            // if it is an admin or manager, return anyway
            if (user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_ADMIN)
            {
                TranscriptDao transcript = transcriptServ.getTranscriptDetails(transID);

                if (transcript != null)
                {
                    return new ResponseEntity<>(new GenericDto<>(null, transcript, null, null), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                }
            }
            //if it is a student, check if it is their application
            else if (user.getRole().getRoleid() == ROLE_STUDENT)
            {
                TranscriptDao transcript = transcriptServ.getTranscriptDetails(transID);

                if (transcript != null)
                {
                    if (Objects.equals(transcript.getStudent().getUserid(), userID))
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, transcript, null, null), HttpStatus.OK);
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

    // create transcript
    @PostMapping("")
    public ResponseEntity<GenericDto<TranscriptDao>> createTranscript(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody TranscriptDao transcriptDao)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_LEADER)
            {
                TranscriptDao createdTranscript = transcriptServ.createTranscript(
                        transcriptDao.getGrade(),
                        transcriptDao.getStudent().getUserid(),
                        transcriptDao.getCourseid()
                );

                return new ResponseEntity<>(new GenericDto<>(null, createdTranscript, null, null), HttpStatus.CREATED);
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

    // edit transcript - not allowed for students
    @PutMapping("")
    public ResponseEntity<GenericDto<TranscriptDao>> editTranscript(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody TranscriptDao transcriptDao)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
            {
                TranscriptDao editedTranscript = transcriptServ.editTranscript(transcriptDao);

                if (editedTranscript != null)
                {
                    return new ResponseEntity<>(new GenericDto<>(null, editedTranscript, null, null), HttpStatus.OK);
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
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    // delete transcript
    @DeleteMapping("/{transID}")
    public ResponseEntity<GenericDto<Void>> deleteTranscript(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("transID") int transID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            // if it is an admin or manager, return anyway
            if (user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_ADMIN)
            {
                if (transcriptServ.deleteTranscript(transID))
                {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            }
            //if it is a student, check if it is their application
            else if (user.getRole().getRoleid() == ROLE_STUDENT)
            {
                TranscriptDao transcript = transcriptServ.getTranscriptDetails(transID);

                if (transcript != null)
                {
                    if (Objects.equals(transcript.getStudent().getUserid(), userID))
                    {
                        if (transcriptServ.deleteTranscript(transID))
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
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
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
}

