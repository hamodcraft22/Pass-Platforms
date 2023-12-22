package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.TranscriptDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.TranscriptServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/transcript")
public class TranscriptCont
{

    @Autowired
    private TranscriptServ transcriptServ;

    // get all transcripts
    @GetMapping("")
    public ResponseEntity<GenericDto<List<TranscriptDao>>> getAllTranscripts(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
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

    @GetMapping("/leader/{leaderID}")
    public ResponseEntity<GenericDto<List<TranscriptDao>>> getLeaderTranscripts(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("leaderID") String leaderID)
    {
        // if it is an admin or manager, return anyway
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY))
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
        else if (Objects.equals(requestKey, STUDENT_KEY))
        {
            List<TranscriptDao> transcripts = transcriptServ.getLeaderTranscripts(leaderID);

            if (transcripts != null && !transcripts.isEmpty())
            {
                if (Objects.equals(requisterID, leaderID))
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

    // get transcript details
    @GetMapping("/{transID}")
    public ResponseEntity<GenericDto<TranscriptDao>> getTranscriptDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("transID") int transID)
    {
        // if it is an admin or manager, return anyway
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY))
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
        else if (Objects.equals(requestKey, STUDENT_KEY))
        {
            TranscriptDao transcript = transcriptServ.getTranscriptDetails(transID);

            if (transcript != null)
            {
                if (Objects.equals(transcript.getStudent().getUserid(), requisterID))
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

    // create transcript
    @PostMapping("")
    public ResponseEntity<GenericDto<TranscriptDao>> createTranscript(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody TranscriptDao transcriptDao)
    {
        if (Objects.equals(requestKey, LEADER_KEY))
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

    // edit transcript - not allowed for students
    @PutMapping("")
    public ResponseEntity<GenericDto<TranscriptDao>> editTranscript(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody TranscriptDao transcriptDao)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
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

    // delete transcript
    @DeleteMapping("/{transID}")
    public ResponseEntity<GenericDto<Void>> deleteTranscript(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("transID") int transID)
    {
        // if it is an admin or manager, return anyway
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY))
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
        else if (Objects.equals(requestKey, STUDENT_KEY))
        {
            TranscriptDao transcript = transcriptServ.getTranscriptDetails(transID);

            if (transcript != null)
            {
                if (Objects.equals(transcript.getStudent().getUserid(), requisterID))
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
}

