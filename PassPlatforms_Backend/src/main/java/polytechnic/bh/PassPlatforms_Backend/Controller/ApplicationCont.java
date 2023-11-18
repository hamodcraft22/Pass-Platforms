package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Entity.Application;
import polytechnic.bh.PassPlatforms_Backend.Service.ApplicationServ;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.*;

@RestController
@RequestMapping("/api/application")
public class ApplicationCont<T> {

    @Autowired
    ApplicationServ applicationServ;

    @GetMapping("")
    public ResponseEntity<GenericDto<T>> getAllApplications(@RequestHeader(value = "Authorization", required = false) String requestKey)
    {
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY))
        {
            //retrieve from service
            List<ApplicationDao> applicationDaos = applicationServ.getAllApplications(false);

            if(!applicationDaos.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, (T) applicationDaos,null), HttpStatus.OK);
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

    @GetMapping("/{applicationID}")
    public ResponseEntity<GenericDto<T>> getApplication(@RequestHeader(value = "Authorization", required = false) String requestKey, @RequestHeader(value = "Requester", required = false) String requisterID, @PathVariable("applicationID") int applicationID)
    {
        // if it is an admin or manager, return anyway
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY))
        {
            ApplicationDao applicationDao = applicationServ.getApplicationDetailsByID(applicationID);

            if (applicationDao != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, (T) applicationDao, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        //if it is a student, check if it is their application
        else if (Objects.equals(requestKey, STUDENT_KEY))
        {
            ApplicationDao applicationDao = applicationServ.getApplicationDetailsByID(applicationID);

            if (applicationDao != null)
            {
                if (Objects.equals(applicationDao.getUser().getUserid(), requisterID))
                {
                    return new ResponseEntity<>(new GenericDto<>(null, (T) applicationDao, null), HttpStatus.OK);
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

    // creating an application (by student);
    @PostMapping("")
    public ResponseEntity<GenericDto<T>> createApplication(@RequestHeader(value = "Authorization", required = false) String requestKey, @RequestHeader(value = "Requester", required = false) String requisterID, @RequestBody String applicationNote)
    {
        if (Objects.equals(requestKey, STUDENT_KEY))
        {
            // check if user has an application first
            ApplicationDao retrivedApplicationDao = applicationServ.getApplicationDetailsByUser(requisterID);

            if (retrivedApplicationDao == null)
            {
                // user has no application, can create a new one

                //** should check if transcript is present **

                if (applicationServ.createApplication(requisterID, applicationNote) != null)
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
                // user already has application
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }


//    @PutMapping("/")
//    public ResponseEntity<GenericDto<T>> updateApplication()
//    {
//        Application applicationtoUpdate = applicationRepo.getReferenceById(1);
//
//        applicationtoUpdate.setNote("has been updated chile");
//        applicationRepo.save(applicationtoUpdate);
//
//        return new ResponseEntity<>(null,HttpStatus.OK);
//    }
//
//    @DeleteMapping("/")
//    public ResponseEntity<GenericDto<T>> deleteApplication()
//    {
//
//
//        return new ResponseEntity<>(null,HttpStatus.OK);
//    }
}
