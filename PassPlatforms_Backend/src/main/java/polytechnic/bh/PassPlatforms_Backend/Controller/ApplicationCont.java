package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Service.ApplicationServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.*;
import static polytechnic.bh.PassPlatforms_Backend.Constant.ApplicationStatusConstant.*;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/application")
public class ApplicationCont
{

    @Autowired
    private ApplicationServ applicationServ;

    @Autowired
    private UserServ userServ;

    @GetMapping("")
    public ResponseEntity<GenericDto<List<ApplicationDao>>> getAllApplications(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            // token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            // check roles for api
            if (user.getRole().getRoleid() == 4 || user.getRole().getRoleid() == 5)
            {
                //retrieve from service
                List<ApplicationDao> applicationDaos = applicationServ.getAllApplications(false);

                if (applicationDaos != null && !applicationDaos.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, applicationDaos, null, null), HttpStatus.OK);
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

    @GetMapping("/{applicationID}")
    public ResponseEntity<GenericDto<ApplicationDao>> getApplication(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("applicationID") int applicationID)
    {
        // if it is an admin or manager, return anyway
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY))
        {
            ApplicationDao applicationDao = applicationServ.getApplicationDetailsByID(applicationID);

            if (applicationDao != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, applicationDao, null, null), HttpStatus.OK);
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
                    return new ResponseEntity<>(new GenericDto<>(null, applicationDao, null, null), HttpStatus.OK);
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
    public ResponseEntity<GenericDto<ApplicationDao>> createApplication(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @RequestBody String applicationNote)
    {
        if (Objects.equals(requestKey, STUDENT_KEY))
        {
            // check if user has an application first
            ApplicationDao retrivedApplicationDao = applicationServ.getApplicationDetailsByUser(requisterID);

            if (retrivedApplicationDao == null)
            {
                // user has no application, can create a new one

                //** should check if transcript is present **//

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

    // update an application (by manager)
    @PutMapping("")
    public ResponseEntity<GenericDto<ApplicationDao>> updateApplication(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @RequestBody ApplicationDao applicationGotten)
    {
        // if it is a student requesting an update, update y student id, if it is a manager update by passed in application id
        if (Objects.equals(requestKey, STUDENT_KEY))
        {
            ApplicationDao retrivedApplicationDao = applicationServ.getApplicationDetailsByUser(requisterID);

            if (retrivedApplicationDao != null)
            {
                // check if it is the student application (extra verification) - did the student pass in his own application
                if (applicationGotten.getUser().getUserid().equals(requisterID))
                {
                    // update application (status only to canceled or reopened by student)
                    char applicationStatus = applicationGotten.getApplicationStatus().getStatusid();

                    if ((retrivedApplicationDao.getApplicationStatus().getStatusid() == APLC_CANCLED && applicationGotten.getApplicationStatus().getStatusid() == APLC_REOPENED) || ((retrivedApplicationDao.getApplicationStatus().getStatusid() != APLC_ACCEPTED && retrivedApplicationDao.getApplicationStatus().getStatusid() != APLC_REJECTED && retrivedApplicationDao.getApplicationStatus().getStatusid() != APLC_CANCLED) && (applicationGotten.getApplicationStatus().getStatusid() == APLC_CANCLED)))
                    {
                        ApplicationDao responseAplDao = applicationServ.updateApplication(retrivedApplicationDao.getApplicationid(), applicationStatus, true);

                        if (responseAplDao != null)
                        {
                            return new ResponseEntity<>(new GenericDto<>(null, responseAplDao, null, null), HttpStatus.OK);
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
            else
            {
                // no application found to update
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY))
        {
            // get application based on the passed in application info
            ApplicationDao retrivedApplicationDao = applicationServ.getApplicationDetailsByID(applicationGotten.getApplicationid());

            if (retrivedApplicationDao != null)
            {
                char applicationStatus = applicationGotten.getApplicationStatus().getStatusid();

                if (applicationStatus != APLC_CREATED && applicationStatus != APLC_CANCLED && applicationStatus != APLC_REOPENED)
                {
                    ApplicationDao responseAplDao = applicationServ.updateApplication(retrivedApplicationDao.getApplicationid(), applicationStatus, false);

                    if (responseAplDao != null)
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, responseAplDao, null, null), HttpStatus.OK);
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
                // no application found to update
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/{applicationID}")
    public ResponseEntity<GenericDto<ApplicationDao>> deleteApplication(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("applicationID") int applicationID
    )
    {
        // only managers and admin are able to fully delete from the db
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY))
        {
            if (applicationServ.deleteApplication(applicationID))
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
}
